package com.example.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerAdapter
    private var tabPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank2, container, false)
    }

    companion object {
        private const val ARG_TAB_POSITION = "tab_position"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(tabPosition: Int): BlankFragment2 {
            val fragment = BlankFragment2()
            val args = Bundle()
            args.putInt(ARG_TAB_POSITION, tabPosition) // Pass the tab position to the fragment
            fragment.arguments = args
            return fragment
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tabPosition = arguments?.getInt(ARG_TAB_POSITION) ?: 0

        // Set data based on the tab position
        when (tabPosition) {
            0 -> adapter = RecyclerAdapter(getDataForTab1())
            1 -> adapter = RecyclerAdapter(getDataForTab2())
            2 -> adapter = RecyclerAdapter(getDataForTab3())
        }

        recyclerView.adapter = adapter
    }

    private fun getDataForTab1(): List<String> {
        return listOf("Item 1", "Item 2", "Item 3")
    }

    private fun getDataForTab2(): List<String> {
        return listOf("Item A", "Item B", "Item C")
    }

    private fun getDataForTab3(): List<String> {
        return listOf("Item X", "Item Y", "Item Z")
    }
}