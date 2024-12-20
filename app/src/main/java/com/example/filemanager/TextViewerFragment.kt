package com.example.filemanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File

class TextViewerFragment : Fragment() {
    companion object {
        private const val ARG_FILE_PATH = "file_path"

        fun newInstance(filePath: String) = TextViewerFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILE_PATH, filePath)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(ARG_FILE_PATH)?.let { path ->
            val file = File(path)
            view.findViewById<TextView>(R.id.textContent).text = file.readText()
        }
    }
}