package com.example.filemanager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private var currentPath: File = Environment.getExternalStorageDirectory()

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        fun newInstance() = FileListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_file_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadFiles()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadFiles()
        }
    }

    private fun loadFiles() {
        val files = currentPath.listFiles()
        Log.d("FileManager", "All files: ${files?.map { it.name }}")

        val filteredFiles = files?.filter { file ->
            val result = file.isDirectory || file.extension.lowercase() in listOf("txt", "log", "json", "xml")
            Log.d("FileManager", "File: ${file.name}, isDir: ${file.isDirectory}, ext: ${file.extension}, passed: $result")
            result
        }

        adapter = FileAdapter(filteredFiles?.toList() ?: emptyList()) { file ->
            when {
                file.isDirectory -> {
                    currentPath = file
                    loadFiles()
                }
                file.extension.lowercase() in listOf("txt", "log", "json", "xml") -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, TextViewerFragment.newInstance(file.absolutePath))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        recyclerView.adapter = adapter

        // Add TextView for empty state
        val emptyText = view?.findViewById<TextView>(R.id.emptyText)
        emptyText?.visibility = if (files.isNullOrEmpty()) View.VISIBLE else View.GONE
    }

    fun handleBackPress(): Boolean {
        return if (currentPath != Environment.getExternalStorageDirectory()) {
            currentPath = currentPath.parentFile!!
            loadFiles()
            true
        } else {
            false
        }
    }
}