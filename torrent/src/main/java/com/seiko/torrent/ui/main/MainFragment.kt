package com.seiko.torrent.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.seiko.common.toast.toast
import com.seiko.torrent.R
import com.seiko.torrent.databinding.TorrentFragmentMainBinding
import com.seiko.torrent.extensions.getClipboard
import com.seiko.torrent.extensions.isHash
import com.seiko.torrent.extensions.isMagnet
import com.seiko.torrent.ui.dialog.BaseAlertDialog
import com.seiko.torrent.ui.dialog.DialogInputFragment
import com.seiko.torrent.util.buildTorrentUri
import kotlinx.android.synthetic.main.torrent_fragment_main.view.*
import news.androidtv.filepicker.TvFilePicker
import java.io.File
import java.util.*

class MainFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: TorrentFragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TorrentFragmentMainBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun setupUI() {
//        binding.addTorrentButton.setClosedOnTouchOutside(true)
//
//        binding.openFileButton.setOnClickListener {
//            binding.addTorrentButton.close(true)
//            startFilePickerActivity()
//        }
//
//        binding.addLinkButton.setOnClickListener {
//            binding.addTorrentButton.close(true)
//            addLinkDialog()
//        }
        binding.torrentBtnAdd.setOnClickListener(this)
        binding.torrentBtnOpenFile.setOnClickListener(this)
        binding.torrentBtnAdd.requestFocus()
    }

    private fun bindViewModel() {

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.torrent_btn_add -> {
                openAddTorrentDialog()
            }
            R.id.torrent_btn_open_file -> {
                startFilePickerActivity()
            }
        }
    }

    private fun openAddTorrentDialog() {
        if (childFragmentManager.findFragmentByTag(DialogInputFragment.TAG) == null) {
            DialogInputFragment.Builder()
                .setHint(getString(R.string.torrent_dialog_add_link_title))
                .setConfirmText(getString(R.string.ok))
                .setCancelText(getString(R.string.cancel))
                .setConfirmClickListener { source ->
                    val uri = buildTorrentUri(source)
                    if (uri == null) {
                        toast("无效的连接：$source")
                    } else {
                        startAddTorrentDialog(uri)
                    }
                }
                .build()
                .show(childFragmentManager)
        }
    }

//    override fun onShow(dialog: AlertDialog?) {
//        if (dialog != null) {
//            val fm = fragmentManager ?: return
//
//            if (fm.findFragmentByTag(TAG_ADD_LINK_DIALOG) != null) {
//                initAddDialog(dialog)
//            }
//        }
//    }


//    private fun initAddDialog(dialog: AlertDialog) {
//        val field = dialog.findViewById<TextInputEditText>(R.id.text_input_dialog)!!
//        val fieldLayout = dialog.findViewById<TextInputLayout>(R.id.layout_text_input_dialog)!!
//
//        /* Dismiss error label if user has changed the text */
//        field.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                /* Nothing */
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                fieldLayout.isErrorEnabled = false
//                fieldLayout.error = null
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                /* Nothing */
//            }
//        })
//
//        /*
//         * It is necessary in order to the dialog is not closed by
//         * pressing positive button if the text checker gave a false result
//         */
//        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//
//        positiveButton.setOnClickListener {
//            if (field.text != null) {
//                val link = field.text!!.toString()
//
//                if (checkEditTextField(link, fieldLayout)) {
//                    val uri = buildTorrentUri(link)
//                    if (uri == null) {
//                        ToastUtils.showShort("无效的连接：$link")
//                        return@setOnClickListener
//                    }
//                    dialog.dismiss()
//                    addTorrentDialog(uri)
//                }
//            }
//        }
//
//        /* Inserting a link from the clipboard */
//        val clipboard = requireActivity().getClipboard() ?: return
//        val text = clipboard.toLowerCase(Locale.US)
//        if (text.isMagnet() || text.isHash()
//            || text.startsWith(HTTP_PREFIX)
//            || text.startsWith(HTTPS_PREFIX)) {
//            field.setText(clipboard)
//            return
//        }
//    }
//
//    private fun addLinkDialog() {
//        val fm = fragmentManager
//        if (fm != null && fm.findFragmentByTag(TAG_ADD_LINK_DIALOG) == null) {
//            val addLinkDialog = BaseAlertDialog.newInstance(
//                getString(R.string.torrent_dialog_add_link_title), null,
//                R.layout.torrent_dialog_text_input,
//                getString(R.string.ok),
//                getString(R.string.cancel), null,
//                this
//            )
//            addLinkDialog.show(fm,
//                TAG_ADD_LINK_DIALOG
//            )
//        }
//    }
//
//    private fun checkEditTextField(s: String?, layout: TextInputLayout?): Boolean {
//        if (s == null || layout == null)
//            return false
//
//        if (s.isNullOrEmpty()) {
//            layout.isErrorEnabled = true
//            layout.error = getString(R.string.torrent_error_empty_link)
//            layout.requestFocus()
//            return false
//        }
//
//        layout.isErrorEnabled = false
//        layout.error = null
//
//        return true
//    }

    private fun startAddTorrentDialog(uri: Uri) {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddTorrentFragment(uri))
    }

    private fun startFilePickerActivity() {
        TvFilePicker.with(this, FilePickerRequestCode)
            .setFilterName("torrent")
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when(requestCode) {
            FilePickerRequestCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = intent?.data ?: return
                    startAddTorrentDialog(uri)
                }
            }
        }
    }

    companion object {
        private const val FilePickerRequestCode = 6906
    }
}