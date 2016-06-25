package bhydrated.android.apps.com.bhydrated;

import android.app.Service;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class Login extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    private EditText editText;
    private AppCompatButton buybut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buybut = (AppCompatButton) findViewById(R.id.buybut);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout); // You must use the layout root
        InputMethodManager im = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);

        SoftKeyboard softKeyboard;
        softKeyboard = new SoftKeyboard(mainLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buybut.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buybut.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
            textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        editText = (EditText) findViewById(R.id.edit_text);

        textInputLayout.setHint(getString(R.string.hint));
        editText.setOnEditorActionListener(ActionListener.newInstance(this));
    }

    private boolean shouldShowError() {
        int textLength = editText.getText().length();
        return textLength > 0 && textLength < 6;
    }

    private void showError() {
        textInputLayout.setError("username is too short");
    }

    private void hideError() {
        textInputLayout.setError("");
    }
    private static final class ActionListener implements TextView.OnEditorActionListener {
        private final WeakReference<Login> LoginWeakReference;

        public static ActionListener newInstance(Login Login) {
            WeakReference<Login> LoginWeakReference = new WeakReference<>(Login);
            return new ActionListener(LoginWeakReference);
        }

        private ActionListener(WeakReference<Login> LoginWeakReference) {
            this.LoginWeakReference = LoginWeakReference;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Login Login = LoginWeakReference.get();
            if (Login != null) {
                if (actionId == EditorInfo.IME_ACTION_GO && Login.shouldShowError()) {
                    Login.showError();
                } else {
                    Login.hideError();
                }
            }
            return true;
        }
    }
}
