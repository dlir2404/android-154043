package vn.edu.hust.studentman

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import vn.edu.hust.studentman.AddStudentFragment.IAddStudentInteraction

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "hoten"
private const val ARG_PARAM2 = "mssv"

/**
 * A simple [Fragment] subclass.
 * Use the [EditStudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditStudentFragment : Fragment() {
    private lateinit var _context: Context;
    private var _interaction: IEditStudentInteraction? = null
    private var _hoten: String? = null
    private var _mssv: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _hoten = it.getString(ARG_PARAM1)
            _mssv = it.getString(ARG_PARAM2)
        }
    }

    interface IEditStudentInteraction {
        fun onCancelEdit()
        fun onEditStudent(hoten: String, mssv: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
        _interaction = context as? IEditStudentInteraction
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_student, container, false)
        val editHoten = view.findViewById<EditText>(R.id.edit_hoten)
        editHoten.setText(_hoten);
        val editMssv = view.findViewById<EditText>(R.id.edit_mssv)
        editMssv.setText(_mssv)

        view.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            _interaction?.onCancelEdit()
        }

        view.findViewById<Button>(R.id.button_ok).setOnClickListener {
            val hoten = editHoten.text.toString()
            val mssv = editMssv.text.toString()
            if (hoten.isEmpty() || mssv.isEmpty()) {
                Toast.makeText(_context, "Name and ID cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            _interaction?.onEditStudent(hoten, mssv)
        }

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(hoten: String, mssv: String) =
            EditStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, hoten)
                    putString(ARG_PARAM2, mssv)
                }
            }
    }
}