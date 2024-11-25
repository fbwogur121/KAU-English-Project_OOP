
package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // btnMenu 클릭 리스너 추가
        binding?.btnMenu?.setOnClickListener {
            // MenuFragment로 전환
            val menuFragment = MenuFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, menuFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}