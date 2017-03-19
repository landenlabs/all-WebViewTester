package com.landenlabs.all_webviewtester.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.landenlabs.all_webviewtester.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Copied original from:
 * http://gnugu.com/node/57
 */

/*
License:
http://creativecommons.org/licenses/by/3.0/legalcode

3. License Grant.

Subject  to  the  terms and conditions of this License, Licensor hereby grants
You  a  worldwide, royalty-free, non-exclusive, perpetual (for the duration of
the applicable copyright) license to exercise the rights in the Work as stated
below:

to  Reproduce  the Work, to incorporate the Work into one or more Collections,
and  to  Reproduce  the Work as incorporated in the Collections; to create and
Reproduce  Adaptations  provided  that  any  such  Adaptation,  including  any
translation  in any medium, takes reasonable steps to clearly label, demarcate
or  otherwise  identify  that  changes  were  made  to  the original Work. For
example,  a translation could be marked "The original work was translated from
English  to  Spanish," or a modification could indicate "The original work has
been  modified.";  to  Distribute  and  Publicly Perform the Work including as
incorporated   in   Collections;  and,  to  Distribute  and  Publicly  Perform
Adaptations. For the avoidance of doubt:

Non-waivable  Compulsory  License Schemes. In those jurisdictions in which the
right  to  collect  royalties  through  any  statutory or compulsory licensing
scheme  cannot be waived, the Licensor reserves the exclusive right to collect
such  royalties  for  any  exercise  by  You  of the rights granted under this
License;  Waivable Compulsory License Schemes. In those jurisdictions in which
the  right  to collect royalties through any statutory or compulsory licensing
scheme  can be waived, the Licensor waives the exclusive right to collect such
royalties  for  any  exercise by You of the rights granted under this License;
and,  Voluntary  License  Schemes.  The  Licensor  waives the right to collect
royalties, whether individually or, in the event that the Licensor is a member
of a collecting society that administers voluntary licensing schemes, via that
society,  from  any  exercise by You of the rights granted under this License.
The  above  rights may be exercised in all media and formats whether now known
or  hereafter  devised.  The  above  rights  include  the  right  to make such
modifications  as  are  technically  necessary to exercise the rights in other
media  and  formats. Subject to Section 8(f), all rights not expressly granted
by Licensor are hereby reserved.
*/

public class ComboBox extends LinearLayout implements EditText.OnEditorActionListener, AdapterView.OnItemSelectedListener   {

    private AutoCompleteTextView m_autoCompleteTv;
    private ImageButton m_showListBtn;
    private ImageView m_clearTextBtn;
    private EditText.OnEditorActionListener m_onActionListener;
    private AdapterView.OnItemSelectedListener m_onItemSelectedListener;

    public ComboBox(Context context) {
        super(context);
        this.createChildControls(context);
    }

    public ComboBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.createChildControls(context);
        commonInit(context, attrs, 0);
    }

    public ComboBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.createChildControls(context);
        commonInit(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ComboBox(Context context, @Nullable AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        this.createChildControls(context);
        commonInit(context, attrs, defStyle);
    }

    ArrayList m_arrayList = new ArrayList();

    private void commonInit(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ComboBox, defStyle, 0);
        try
        {
            CharSequence[] entries = a.getTextArray(R.styleable.ComboBox_android_entries);
            if (entries != null) {
                m_arrayList.addAll(Arrays.asList(entries));
                m_autoCompleteTv.setAdapter(new ArrayAdapter<CharSequence>(context, android.R.layout.simple_dropdown_item_1line, m_arrayList));
            }

            /*  How to parse textSize 12sp
            int textSize = a.getInt(R.styleable.ComboBox_android_textSize, -1);
            if (textSize != -1)
                m_autoCompleteTv.setTextSize(0, textSize);
            */
            String hint = "";
            try {
                hint = a.getString(R.styleable.ComboBox_android_hint);
                if (hint == null) {
                    CharSequence cs = a.getText(R.styleable.ComboBox_android_hint);
                    if (cs != null)
                        hint = cs.toString();
                }
            } catch (Exception ex) {
                hint = a.getNonResourceString(R.styleable.ComboBox_android_hint);
            }

            if (hint != null)
                m_autoCompleteTv.setHint(hint);

            final boolean selectOnly = a.getBoolean(R.styleable.ComboBox_selectOnly, false);
            m_autoCompleteTv.setEnabled(!selectOnly);

            final int textSize = a.getDimensionPixelSize(R.styleable.ComboBox_android_textSize, 0);
            if (textSize != 0) {
                m_autoCompleteTv.setTextSize(textSize);
            }
        }
        finally
        {
            a.recycle();
        }
    }

    private void createChildControls(Context context) {
        this.setOrientation(HORIZONTAL);
        this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        m_autoCompleteTv = new AutoCompleteTextView(context);
        m_autoCompleteTv.setSingleLine();
        m_autoCompleteTv.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_NORMAL
                | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
                | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        m_autoCompleteTv.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        m_autoCompleteTv.setOnEditorActionListener(this);
        m_autoCompleteTv.setOnItemSelectedListener(this);

        this.addView(m_autoCompleteTv, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, 1));

        m_clearTextBtn = new ImageButton(context);
        m_clearTextBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        m_clearTextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                m_autoCompleteTv.setText("");
            }
        });

        m_clearTextBtn.setPadding(4,4,4,4);
        this.addView(m_clearTextBtn, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));

        m_showListBtn = new ImageButton(context);
        m_showListBtn.setImageResource(android.R.drawable.arrow_down_float);
        m_showListBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                m_autoCompleteTv.showDropDown();
            }
        });
        this.addView(m_showListBtn, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (m_onActionListener != null)
            return m_onActionListener.onEditorAction(v, actionId, event);
        return false;
    }

    public void setOnEditorActionListener(EditText.OnEditorActionListener onActionListener) {
        m_onActionListener = onActionListener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (m_onItemSelectedListener != null)
            m_onItemSelectedListener.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (m_onItemSelectedListener != null)
            m_onItemSelectedListener.onNothingSelected(parent);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        m_onItemSelectedListener = onItemSelectedListener;
    }

    /**
     * Sets the source for DDLB suggestions.
     * Cursor MUST be managed by supplier!!
     * @param source Source of suggestions.
     * @param column Which column from source to show.
     */
    public void setSuggestionSource(Cursor source, String column) {
        String[] from = new String[] { column };
        int[] to = new int[] { android.R.id.text1 };
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this.getContext(),
                android.R.layout.simple_dropdown_item_1line, source, from, to);
        // this is to ensure that when suggestion is selected
        // it provides the value to the textbox
        cursorAdapter.setStringConversionColumn(source.getColumnIndex(column));
        m_autoCompleteTv.setAdapter(cursorAdapter);
    }

    /**
     * Gets the text in the combo box.
     *
     * @return Text.
     */
    public String getText() {
        return m_autoCompleteTv.getText().toString();
    }

    /**
     * Sets the text in combo box (adds if not already in autocomplete list).
     */
    public void setText(String text) {
        if (TextUtils.isEmpty(text))
            return;

        for (int idx = 0; idx < m_autoCompleteTv.getAdapter().getCount(); idx++) {
            if (text.equals(m_autoCompleteTv.getAdapter().getItem(idx))) {
                m_autoCompleteTv.setText(text);
                return;
            }
        }

        int cnt = m_arrayList.size();
        m_arrayList.add(text);
        ((ArrayAdapter)m_autoCompleteTv.getAdapter()).notifyDataSetChanged();
        m_autoCompleteTv.setText(text);
    }


    public int getListSelection() {
        return m_autoCompleteTv.getListSelection();
    }

    public void setListSelection(int pos) {
        m_autoCompleteTv.setListSelection(pos);
    }
}