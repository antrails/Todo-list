
package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.IntentFilter
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addMemoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class addMemoFragment : Fragment() {
    private lateinit var buttonDelete: Button
    private lateinit var checkBoxIsDone: CheckBox
    private lateinit var buttonCancel: Button
    private lateinit var buttonSave: Button
    private lateinit var editTextTime: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var editTextContext: EditText
    private lateinit var editTextTitle: EditText

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragView =  inflater.inflate(R.layout.fragment_add_memo, container, false)
        editTextTitle = fragView.findViewById<EditText>(R.id.editTextText_title)
        editTextContext = fragView.findViewById<EditText>(R.id.editTextText_context)
        editTextLocation = fragView.findViewById<EditText>(R.id.editTextText_location)
        editTextDate = fragView.findViewById<EditText>(R.id.editTextDate)
        editTextTime = fragView.findViewById<EditText>(R.id.editTextTime)
        checkBoxIsDone = fragView.findViewById<CheckBox>(R.id.checkBox)
        buttonSave = fragView.findViewById<Button>(R.id.button_save)
        buttonCancel = fragView.findViewById<Button>(R.id.button_cancel)
        buttonDelete = fragView.findViewById<Button>(R.id.button_delete)

        val title = arguments?.getString("title").orEmpty()
        val context = arguments?.getString("context").orEmpty()
        val date = arguments?.getString("date").orEmpty()
        val time = arguments?.getString("time").orEmpty()
        val location = arguments?.getString("location").orEmpty()
        val isDone = arguments?.getBoolean("isDone",false) ?: false
        checkBoxIsDone.isChecked = isDone
        val position = arguments?.getInt("position", -1) ?: -1
        editTextTitle.setText(title)
        editTextContext.setText(context)
        editTextDate.setText(date)
        editTextTime.setText(time)
        editTextLocation.setText(location)


        buttonDelete.setOnClickListener {
            val position = arguments?.getInt("position", -1) ?: -1
            val id = arguments?.getInt("id", 0) ?: 0
            val title = arguments?.getString("title").orEmpty()
            val context = arguments?.getString("context").orEmpty()
            val date = arguments?.getString("date").orEmpty()
            val time = arguments?.getString("time").orEmpty()
            val location = arguments?.getString("location").orEmpty()
            val isDone = arguments?.getBoolean("isDone", false) ?: false

            val bundle = Bundle().apply {
                putInt("id", id)
                putInt("position", position)
                putString("title", title)
                putString("context", context)
                putString("date", date)
                putString("time", time)
                putString("location", location)
                putBoolean("isDone", isDone)
                putBoolean("isDelete", true)
            }
            parentFragmentManager.setFragmentResult("addMission",bundle)
            parentFragmentManager.popBackStack()

        }



        buttonCancel.setOnClickListener {
            editTextTitle.setText("")
            editTextContext.setText("")
            editTextLocation.setText("")
            editTextDate.setText("")
            editTextTime.setText("")
        }

        buttonSave.setOnClickListener {
            val title = editTextTitle.text.toString()
            val context = editTextContext.text.toString()
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()
            val location = editTextLocation.text.toString()
            val isDone = checkBoxIsDone.isChecked
            val position = arguments?.getInt("position", -1) ?: -1
            val id = arguments?.getInt("id", 0) ?:0


            val bundle = Bundle().apply {
                putInt("id",id)
                putString("title",title)
                putString("context", context)
                putString("date", date)
                putString("time", time)
                putString("location", location)
                putBoolean("isDone",isDone)
                putInt("position",position)
            }
            parentFragmentManager.setFragmentResult("addMission",bundle)
            parentFragmentManager.popBackStack()

        }

        editTextDate.inputType = 0
        editTextDate.isFocusable = false
        editTextDate.setOnClickListener {
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(),{_, selectYear,selectMonth,selectDay ->
                val date = String.format("%04d/%02d/%02d",selectYear,selectMonth + 1,selectDay)
                editTextDate.setText(date)
            },year,month,day).show()
        }

        editTextTime.inputType = 0
        editTextTime.isFocusable = false

        editTextTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                editTextTime.setText(time)
            }, hour, minute, true).show()
        }

        checkBoxIsDone.setOnClickListener {
            if (checkBoxIsDone.isChecked) checkBoxIsDone.text = "已完成"
            else checkBoxIsDone.text = "尚未完成"
        }


        return fragView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addMemoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addMemoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}