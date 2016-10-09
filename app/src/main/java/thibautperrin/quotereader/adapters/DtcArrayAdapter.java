package thibautperrin.quotereader.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import thibautperrin.quotereader.MainActivityHandler;
import thibautperrin.quotereader.R;
import thibautperrin.quotereader.bean.Dtc;
import thibautperrin.quotereader.bean.Sentence;
import thibautperrin.quotereader.dao.DaoDtc;


public class DtcArrayAdapter extends ArrayAdapter<Dtc> implements MainActivityHandler.NewQuoteReceiver {

    private final Context context;
    private final List<Dtc> dtcs;

    public DtcArrayAdapter(Context context, int resource, List<Dtc> dtcs) {
        super(context, resource, dtcs);
        this.context = context;
        this.dtcs = dtcs;
        loadDtcs();
        MainActivityHandler.setDtcsReceiver(this);
    }

    private void loadDtcs() {
        DaoDtc daoDtc = new DaoDtc(context);
        daoDtc.open();
        List<Dtc> loadedDtcs = daoDtc.getDtc();
        daoDtc.close();
        addAll(loadedDtcs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Dtc dtc = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dtc_view, parent, false);
        }
        TextView textViewContent = (TextView) convertView.findViewById(R.id.dtc_content);
        TextView textViewNumber= (TextView) convertView.findViewById(R.id.dtc_number);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (Sentence sentence : dtc.getContent()) {
            if (builder.length() != 0) {
                builder.append("\n");
            }
            String author = sentence.getAuthor();
            String message = sentence.getContent();
            Spannable word = new SpannableString(author + message);
            word.setSpan(new ForegroundColorSpan(Color.GREEN), 0, author.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            word.setSpan(new StyleSpan(Typeface.BOLD), 0, author.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            word.setSpan(new ForegroundColorSpan(Color.WHITE), author.length(), word.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            builder.append(word);
        }
        textViewContent.setText(builder);
        textViewNumber.setText(String.format(Locale.getDefault(), "%d", dtc.getNumber()));
        return convertView;
    }

    @Override
    public void addAll(@NonNull Collection<? extends Dtc> collection) {
        for (Dtc dtc : collection) {
            if (!this.dtcs.contains(dtc)) {
                this.dtcs.add(dtc);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(dtcs);
        super.notifyDataSetChanged();
    }

    @Override
    public void onReceive() {
        loadDtcs();
    }
}
