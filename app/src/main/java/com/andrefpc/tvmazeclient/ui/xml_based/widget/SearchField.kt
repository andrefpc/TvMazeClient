package com.andrefpc.tvmazeclient.ui.xml_based.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.databinding.SearchFieldBinding
import com.andrefpc.tvmazeclient.core.extensions.ViewExtensions.fadeIn
import com.andrefpc.tvmazeclient.core.extensions.ViewExtensions.fadeOut
import com.andrefpc.tvmazeclient.core.extensions.ViewExtensions.hideKeyboard

@SuppressLint("Recycle")
class SearchField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: SearchFieldBinding =
        SearchFieldBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * Init Callbacks
     */
    private var keyBoardActionListener: () -> Unit = {}
    private var textChangeListener: (String) -> Unit = {}
    private var focusChangeListener: (Boolean) -> Unit = {}
    private var clearListener: () -> Unit = {}

    /**
     * Listener for Keyboard Actions using EditorInfo params
     */
    fun onKeyboardAction(keyBoardActionListener: () -> Unit) {
        this.keyBoardActionListener = keyBoardActionListener
    }

    /**
     * Listener to trigger text changes
     */
    fun onTextChange(textChangeListener: (String) -> Unit) {
        this.textChangeListener = textChangeListener
    }

    /**
     * Listener to trigger focus changes
     */
    fun onFocusChange(focusChangeListener: (Boolean) -> Unit) {
        this.focusChangeListener = focusChangeListener
    }

    /**
     * Listener to trigger clear click
     */
    fun onClear(clearListener: () -> Unit) {
        this.clearListener = clearListener
    }

    // Params Getters and Setters
    /**
     * Display text on TextInputEditText
     */
    var text: String = ""
        get() {
            return binding.zeeSearchField.text.toString()
        }
        set(value) {
            field = value
            binding.zeeSearchField.setText(value)
        }

    /**
     * Set hint value (placeholder) for TextInputLayout
     */
    var hint: String = ""
        set(value) {
            field = value
            binding.zeeSearchField.hint = value
        }

    /**
     * Widget Initializer
     */
    init {
        initViews(attrs, context)
        initListeners()
    }

    /**
     * Method to initialize views using widget Attributes
     */
    private fun initViews(attrs: AttributeSet?, context: Context) {
        if (attrs != null) {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.SearchField)
            // Set hint
            attr.getString(R.styleable.SearchField_search_field_hint)?.let { hint = it }

            // Set helperText
            attr.getString(R.styleable.SearchField_search_field_text)?.let { text = it }
        }
    }

    /**
     * Method to initialize widget listeners
     */
    private fun initListeners() {
        binding.zeeSearchField.setOnEditorActionListener { _, _, _ ->
            hideKeyboard()
            keyBoardActionListener()
            false
        }

        binding.zeeSearchField.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(
                    text: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (text.isEmpty()) {
                        if (binding.zeeSearchClear.visibility == View.VISIBLE) {
                            binding.zeeSearchClear.fadeOut()
                        }
                    } else {
                        if (binding.zeeSearchClear.visibility == View.GONE) {
                            binding.zeeSearchClear.fadeIn()
                        }
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                    textChangeListener(editable.toString())
                }
            }
        )

        binding.zeeSearchField.setOnFocusChangeListener { _, isFocused ->
            focusChangeListener(isFocused)
        }

        binding.zeeSearchClear.setOnClickListener {
            text = ""
            clearListener()
        }
    }

    /**
     * Method to hide the keyboard
     */
    private fun hideKeyboard() {
        binding.zeeSearchField.clearFocus()
        binding.zeeSearchField.hideKeyboard()
    }
}
