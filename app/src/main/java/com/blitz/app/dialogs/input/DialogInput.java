package com.blitz.app.dialogs.input;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialogFragment;

import butterknife.InjectView;

/**
 * Created by Miguel on 11/1/2014. Copyright 2014 Blitz Studios
 */
public class DialogInput extends BaseDialogFragment {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.dialog_input_header)     TextView mInputHeader;
    @InjectView(R.id.dialog_input_text_label) TextView mInputTextLabel;
    @InjectView(R.id.dialog_input_text)       EditText mInputText;
    @InjectView(R.id.dialog_input_button_left)  Button mInputButtonLeft;
    @InjectView(R.id.dialog_input_button_right) Button mInputButtonRight;

    private int mInputHeaderResourceId;
    private int mInputTextLabelResourceId;
    private int mInputTextResourceId;
    private int mInputButtonLeftResourceId;
    private int mInputButtonRightResourceId;

    // Callbacks handler.
    private Callbacks mCallbacks;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Configure the view when created.
     *
     * @param view Created view.
     */
    @Override
    protected void onViewCreated(View view) {

        // Set left button click listener.
        mInputButtonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mCallbacks != null) {
                    mCallbacks.onDialogButtonLeftPressed(DialogInput.this);
                }
            }
        });

        // Set right button click listener.
        mInputButtonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mCallbacks != null) {
                    mCallbacks.onDialogButtonRightPressed(DialogInput.this);
                }
            }
        });

        // Set the text of the UI.
        mInputHeader
                .setText(mInputHeaderResourceId);
        mInputTextLabel
                .setText(mInputTextLabelResourceId);
        mInputText
                .setHint(mInputTextResourceId);
        mInputButtonLeft
                .setText(mInputButtonLeftResourceId);
        mInputButtonRight
                .setText(mInputButtonRightResourceId);
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Create a new instance.
     *
     * @param callbacks Callbacks object.
     *
     * @return Input dialog instance.
     */
    @SuppressWarnings("unused")
    public static DialogInput create(Callbacks callbacks,
            int inputHeaderResourceId,
            int inputTextLabelResourceId,
            int inputTextResourceId,
            int inputButtonLeftResourceId,
            int inputButtonRightResourceId) {

        DialogInput dialogInput = new DialogInput();

        // Set the callbacks.
        dialogInput.mCallbacks = callbacks;

        dialogInput.mInputHeaderResourceId      = inputHeaderResourceId;
        dialogInput.mInputTextLabelResourceId   = inputTextLabelResourceId;
        dialogInput.mInputTextResourceId        = inputTextResourceId;
        dialogInput.mInputButtonLeftResourceId  = inputButtonLeftResourceId;
        dialogInput.mInputButtonRightResourceId = inputButtonRightResourceId;

        return dialogInput;
    }

    /**
     * Fetch inputted text.
     *
     * @return Inputted text.
     */
    @SuppressWarnings("unused")
    public String getInputtedText() {

        // Fetch text inputted by the user if exists.
        return mInputText != null ? mInputText.getText().toString() : "";
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks {

        public void onDialogButtonLeftPressed(DialogInput dialogInput);
        public void onDialogButtonRightPressed(DialogInput dialogInput);
    }

    // endregion
}