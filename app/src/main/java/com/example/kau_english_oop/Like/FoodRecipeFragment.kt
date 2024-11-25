package com.example.kau_english_oop.Like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentFoodRecipeBinding
import com.example.kau_english_oop.databinding.FragmentScienceJournalBinding


// 음식 레시피 웹 페이지를 표시하는 Fragment
class FoodRecipeFragment : Fragment() {
    private var binding: FragmentFoodRecipeBinding? = null // ViewBinding 객체 선언


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화: XML 레이아웃을 확장하여 ViewBinding 객체 생성
        binding = FragmentFoodRecipeBinding.inflate(inflater, container, false)


        // WebView 설정
        binding?.webView?.webViewClient = WebViewClient() // WebView에서 외부 브라우저가 아닌 WebView 자체에서 페이지를 열도록 설정


        // 웹 페이지 로드 (New York Times Cooking 웹 페이지)
        binding?.webView?.loadUrl("https://cooking.nytimes.com/")  // WebView에서 로드할 URL 설정

        // Fragment의 루트 뷰 반환
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // 메모리 누수를 방지하기 위해 binding을 해제
    }
}