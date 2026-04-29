package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class CalenderFragment : Fragment() {

    private lateinit var adapter: MissionAdapter
    private lateinit var missionDao: MissionDao
    private lateinit var addButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewTaskCount: TextView
    private lateinit var textViewSelectedDate: TextView
    private lateinit var calendarView: CalendarView

    private val missionList = mutableListOf<Mission>()
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragView = inflater.inflate(R.layout.fragment_calender, container, false)

        calendarView = fragView.findViewById(R.id.calendarView)
        textViewSelectedDate = fragView.findViewById(R.id.textViewSelectedDate)
        textViewTaskCount = fragView.findViewById(R.id.textViewTaskCount)
        recyclerView = fragView.findViewById(R.id.recyclerView_calendarTasks)
        addButton = fragView.findViewById(R.id.floatingActionButton_addCalendarMemo)

        missionDao = DatabaseProvider.getDatabase(requireContext()).missionDao()

        selectedDate = getTodayDate()

        adapter = MissionAdapter(missionList) { mission, position ->
            openEditMission(mission, position)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        parentFragmentManager.setFragmentResultListener("addMission", viewLifecycleOwner) { _, bundle ->

            val id = bundle.getInt("id", 0)
            val position = bundle.getInt("position", -1)
            val isDelete = bundle.getBoolean("isDelete", false)

            val mission = Mission(
                id = id,
                title = bundle.getString("title", "").orEmpty(),
                context = bundle.getString("context", "").orEmpty(),
                date = bundle.getString("date", "").orEmpty(),
                time = bundle.getString("time", "").orEmpty(),
                location = bundle.getString("location", "").orEmpty(),
                isDone = bundle.getBoolean("isDone", false),
                userAccount = SessionManager.currentUser?.account.orEmpty()
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (isDelete) {
                        missionDao.deleteMission(mission)
                        FirebaseMissionRepository.deleteMission(mission)
                    } else if (position != -1) {
                        missionDao.updateMission(mission)
                        FirebaseMissionRepository.uploadMission(mission)
                    } else {
                        val newId = missionDao.insertMission(mission).toInt()
                        val missionWithId = mission.copy(id = newId)
                        FirebaseMissionRepository.uploadMission(missionWithId)
                    }
                }
                loadMissionsByDate(selectedDate)
            }
        }



        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "%04d/%02d/%02d".format(year, month + 1, dayOfMonth)
            loadMissionsByDate(selectedDate)
        }

        addButton.setOnClickListener {
            openAddMission()
        }

        return fragView
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized && ::missionDao.isInitialized && selectedDate.isNotEmpty()) {
            loadMissionsByDate(selectedDate)
        }
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "%04d/%02d/%02d".format(year, month, day)
    }

    private fun loadMissionsByDate(date: String) {
        val account = SessionManager.currentUser?.account ?: return

        lifecycleScope.launch {
            val missions = withContext(Dispatchers.IO) {
                missionDao.getMissionsByUserAndDate(account, date)
            }

            missionList.clear()
            missionList.addAll(missions)

            textViewSelectedDate.text = date
            textViewTaskCount.text = "目前有 ${missionList.size} 個任務"

            adapter.notifyDataSetChanged()
        }
    }

    private fun openAddMission() {
        val fragment = addMemoFragment()

        val bundle = Bundle().apply {
            putString("date", selectedDate)
        }

        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout_id, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openEditMission(mission: Mission, position: Int) {
        val fragment = addMemoFragment()

        val bundle = Bundle().apply {
            putInt("id", mission.id)
            putString("title", mission.title)
            putString("context", mission.context)
            putString("date", mission.date)
            putString("time", mission.time)
            putString("location", mission.location)
            putBoolean("isDone", mission.isDone)
            putInt("position", position)
        }

        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout_id, fragment)
            .addToBackStack(null)
            .commit()
    }
}