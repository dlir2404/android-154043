package vn.edu.hust.studentman

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar

class ListStudentFragment : Fragment() {
    private lateinit var _students: MutableList<StudentModel>;
    private lateinit var _studentAdapter: StudentAdapter;
    private lateinit var _context: Context;
    private var _interaction: IListStudentInteraction? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface IListStudentInteraction {
        fun getListStudent(): MutableList<StudentModel>
        fun setStudentAdapter(studentAdapter: StudentAdapter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _context = context
        _interaction = context as? IListStudentInteraction
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_student, container, false)

        if (_interaction != null) {
            _students = _interaction!!.getListStudent()
            _studentAdapter = StudentAdapter(_students, _context)

            _interaction!!.setStudentAdapter(_studentAdapter)

            val listView = view.findViewById<ListView>(R.id.list_view);
            listView.adapter = _studentAdapter
            registerForContextMenu(listView);
        }

        return view
    }
}