package com.example.todolist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreenFragment : Fragment() {
    private val missionList = mutableListOf<Mission>()
    private lateinit var adapter: MissionAdapter

    private lateinit var missionDao: MissionDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var addMemoButton: FloatingActionButton

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    private fun loadMissions() {
        val currentAccount = SessionManager.currentUser?.account?:return
        lifecycleScope.launch {
            val missionsFromDb = withContext(Dispatchers.IO) {
                missionDao.getAllMissionsByUser(currentAccount)
            }

            missionList.clear()
            missionList.addAll(missionsFromDb)
            adapter.notifyDataSetChanged()
        }
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView = inflater.inflate(R.layout.fragment_home_screen, container, false)

        addMemoButton = fragView.findViewById<FloatingActionButton>(R.id.floatingActionButton_addMemo)
        recyclerView = fragView.findViewById<RecyclerView>(R.id.recyclerView_id)

        missionDao = DatabaseProvider.getDatabase(requireContext()).missionDao() //去資料庫裡拿到操作任務的工具。


        adapter = MissionAdapter(missionList) { mission,position ->
            val bundle = Bundle().apply {
                putInt("id", mission.id)
                putString("title", mission.title)
                putString("context", mission.context)
                putString("date", mission.date)
                putString("time", mission.time)
                putString("location", mission.location)
                putBoolean("isDone",mission.isDone)
                putInt("position", position)
            }
            val fragment = addMemoFragment()
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_id, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadMissions()

        addMemoButton.setOnClickListener {
            if (SessionManager.currentUser == null) {
                Toast.makeText(requireContext(), "請先登入", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_id, addMemoFragment())
                .addToBackStack(null)
                .commit()
        }

        parentFragmentManager.setFragmentResultListener("addMission",viewLifecycleOwner) {_, bundle->

            val id = bundle.getInt("id",0)
            val position = bundle.getInt("position", -1)
            val isDelete = bundle.getBoolean("isDelete",false)

            val mission = Mission(
                id = id,
                title = bundle.getString("title", "").orEmpty(),
                context = bundle.getString("context", "").orEmpty(),
                date = bundle.getString("date", "").orEmpty(),
                time = bundle.getString("time", "").orEmpty(),
                location = bundle.getString("location", "").orEmpty(),
                isDone = bundle.getBoolean("isDone",false),
                userAccount = SessionManager.currentUser?.account.orEmpty()
            )


//            Log.d("FirebaseTest", "按鈕/儲存事件觸發")

            requireActivity().lifecycleScope.launch {
//                Log.d("FirebaseTest", "進入 Activity lifecycleScope")

                try {
                    withContext(Dispatchers.IO) {
//                        Log.d("FirebaseTest", "進入 IO，mission id=${mission.id}")

                        if (isDelete) {
//                            Log.d("FirebaseTest", "準備刪除 Room mission id=${mission.id}, user=${mission.userAccount}")

                            missionDao.deleteMission(mission)

//                            Log.d("FirebaseTest", "Room 刪除完成 mission id=${mission.id}")
//
//                            Log.d("FirebaseTest", "準備刪除 Firebase mission id=${mission.id}, user=${mission.userAccount}")

                            FirebaseMissionRepository.deleteMission(mission)

//                            Log.d("FirebaseTest", "Firebase 刪除完成 mission id=${mission.id}")
                        } else if (position != -1) {
//                            Log.d("FirebaseTest", "準備更新 mission id=${mission.id}, user=${mission.userAccount}")

                            missionDao.updateMission(mission)
                            FirebaseMissionRepository.uploadMission(mission)

//                            Log.d("FirebaseTest", "更新完成 mission id=${mission.id}")

                        } else {
//                            Log.d("FirebaseTest", "準備新增 mission")

                            val newId = missionDao.insertMission(mission).toInt()
                            val missionWithId = mission.copy(id = newId)

//                            Log.d("FirebaseTest", "Room 新增完成 id=$newId, user=${missionWithId.userAccount}")

                            FirebaseMissionRepository.uploadMission(missionWithId)

//                            Log.d("FirebaseTest", "Firebase 新增完成 id=$newId")
                        }
                    }

                    loadMissions()

                } catch (e: Exception) {
//                    Log.e("FirebaseTest", "同步失敗", e)
                }
            }

        }
        return fragView
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized && ::missionDao.isInitialized) {
            loadMissions()
        }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}