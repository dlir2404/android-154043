package vn.edu.hust.studentman

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class AddStudentFragment : Fragment() {
    private var _context: IAddStudentInteraction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface IAddStudentInteraction {
        fun onCancelAdd()
        fun onAddStudent(hoten: String, mssv: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context as? IAddStudentInteraction
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_student, container, false)

        view.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            _context?.onCancelAdd()
        }

        view.findViewById<Button>(R.id.button_ok).setOnClickListener {
            val editHoten = view.findViewById<EditText>(R.id.edit_hoten)
            val editMssv = view.findViewById<EditText>(R.id.edit_mssv)
            val hoten = editHoten.text.toString()
            val mssv = editMssv.text.toString()

            _context?.onAddStudent(hoten, mssv)
        }

        return view
    }
}