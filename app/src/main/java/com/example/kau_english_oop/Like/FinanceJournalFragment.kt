package com.example.kau_english_oop.Like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentFinanceBinding
import com.example.kau_english_oop.databinding.FragmentFinanceJournalBinding

// 금융 저널 웹 페이지를 표시하는 Fragment
class FinanceJournalFragment : Fragment() {
    private var binding: FragmentFinanceJournalBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        binding = FragmentFinanceJournalBinding.inflate(inflater, container, false)

        // WebView 설정
        binding?.webView?.webViewClient = WebViewClient() // WebView에서 새 창이 아닌 WebView 자체에서 페이지를 로드
        binding?.webView?.settings?.javaScriptEnabled = true // 필요 시 JavaScript 활성화

        // 웹 페이지 로드 (예: Wall Street Journal의 금융 섹션)
        binding?.webView?.loadUrl("https://www.wsj.com/finance?mod=nav_top_section") // 원하는 URL로 변경

        // Fragment의 루트 뷰 반환
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // 메모리 누수를 방지하기 위해 binding을 해제
    }


}