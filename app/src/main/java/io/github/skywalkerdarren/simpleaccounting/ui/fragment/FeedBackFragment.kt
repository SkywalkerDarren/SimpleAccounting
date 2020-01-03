package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.lifecycle.ViewModelProviders
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.base.BaseFragment
import io.github.skywalkerdarren.simpleaccounting.databinding.FragmentFeedBackBinding
import io.github.skywalkerdarren.simpleaccounting.util.ViewModelFactory
import io.github.skywalkerdarren.simpleaccounting.view_model.FeedBackViewModel

class FeedBackFragment : BaseFragment() {
    companion object {
        const val TAG = "FeedBackFragment"
        fun newInstance() = FeedBackFragment()
    }

    private lateinit var binding: FragmentFeedBackBinding

    private lateinit var viewModel: FeedBackViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_feed_back, container, false)
        val url = "https://support.qq.com/product/113692"

        binding = FragmentFeedBackBinding.bind(root)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.toolbar.collapseIcon
        binding.title.text = getString(R.string.feedback)
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    binding.progress.visibility = View.GONE
                } else {
                    binding.progress.progress = newProgress
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (title == null || title.contains("http")) {
                    return
                }
                binding.title.text = title
                Log.d(TAG, "onReceivedTitle: $title")
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
                fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragment_container, MyAccountFragment.newInstance())
                        ?.commit()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = request?.url ?: return false
                if (uri.toString().contains("weixin://")) {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    view?.context?.startActivity(intent)
                    return true
                }
                return false
            }
        }

        fun close() {
            fragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragment_container, MyAccountFragment.newInstance())
                    ?.commit()
        }

        binding.webView.loadUrl(url)
        binding.close.setOnClickListener {
            close()
        }
        binding.back.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                close()
            }
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProviders.of(this, factory).get(FeedBackViewModel::class.java)
        binding.feedback = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // TODO: Use the ViewModel
    }

    override fun updateUI() {

    }
}