package com.example.kau_english_oop.Bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.Camera.MyDiaryFragment
import com.example.kau_english_oop.R
import com.example.kau_english_oop.Camera.SearchFragment
import com.example.kau_english_oop.Camera.WriteFragment
import com.example.kau_english_oop.databinding.FragmentCameraBinding



class CameraFragment : Fragment() {

    private var binding: FragmentCameraBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding?.root

        binding?.btnMyDiary?.setOnClickListener {
            setSelectedButton(binding?.btnMyDiary, R.drawable.diary_selected)
            resetOtherButtons(binding?.btnWrite to R.drawable.write_default, binding?.btnSearch to R.drawable.search_default)
            navigateToFragment(MyDiaryFragment())
        }

        binding?.btnWrite?.setOnClickListener {
            setSelectedButton(binding?.btnWrite, R.drawable.write_selected)
            resetOtherButtons(binding?.btnMyDiary to R.drawable.diary_default, binding?.btnSearch to R.drawable.search_default)
            navigateToFragment(WriteFragment())
        }

        binding?.btnSearch?.setOnClickListener {
            setSelectedButton(binding?.btnSearch, R.drawable.search_selected)
            resetOtherButtons(binding?.btnMyDiary to R.drawable.diary_default, binding?.btnWrite to R.drawable.write_default)
            navigateToFragment(SearchFragment())
        }

        return binding?.root
    }

    private fun setSelectedButton(button: ImageButton?, selectedDrawable: Int) {
        button?.setImageResource(selectedDrawable)
    }

    private fun resetOtherButtons(vararg buttonDrawablePairs: Pair<ImageButton?, Int>) {
        buttonDrawablePairs.forEach { (button, drawable) ->
            button?.setImageResource(drawable)
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction() // 자식 프래그먼트 관리자를 사용하여 교체
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}



