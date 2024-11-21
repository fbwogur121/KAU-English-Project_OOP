package com.example.kau_english_oop.Camera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.kau_english_oop.R
import com.example.kau_english_oop.WriteFragment
import com.example.kau_english_oop.databinding.FragmentCameraBinding
import com.example.kau_english_oop.databinding.FragmentTimeArrangeBinding

class TimeArrangeFragment : Fragment() {

    private var binding: FragmentTimeArrangeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimeArrangeBinding.inflate(inflater, container, false)
        val view = binding?.root

        binding?.btn2023?.setOnClickListener {
            setSelectedButton(binding?.btn2023, R.drawable.button_2023_selected)
            resetOtherButtons(binding?.btn2024 to R.drawable.button_2024_unselected,
                binding?.btn2025 to R.drawable.button_2025_unselected, binding?.btn2026 to R.drawable.button_2026_unselected)
            navigateToFragment(BlankFragment())
        }

        binding?.btn2024?.setOnClickListener {
            setSelectedButton(binding?.btn2024, R.drawable.button_2024_selected)
            resetOtherButtons(binding?.btn2023 to R.drawable.button_2023_unselected,
                binding?.btn2025 to R.drawable.button_2025_unselected, binding?.btn2026 to R.drawable.button_2026_unselected)
            navigateToFragment(ThisYearFragment())
        }

        binding?.btn2025?.setOnClickListener {
            setSelectedButton(binding?.btn2025, R.drawable.button_2025_selected)
            resetOtherButtons(binding?.btn2023 to R.drawable.button_2023_unselected,
                binding?.btn2024 to R.drawable.button_2024_unselected, binding?.btn2026 to R.drawable.button_2026_unselected)
            navigateToFragment(BlankFragment())
        }

        binding?.btn2026?.setOnClickListener {
            setSelectedButton(binding?.btn2026, R.drawable.button_2026_selected)
            resetOtherButtons(binding?.btn2023 to R.drawable.button_2023_unselected,
                binding?.btn2024 to R.drawable.button_2024_unselected, binding?.btn2025 to R.drawable.button_2025_unselected)
            navigateToFragment(BlankFragment())
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