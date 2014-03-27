package cl.lay.azurevmoff.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cl.lay.azurevmoff.R;
import cl.lay.azurevmoff.models.AccountModel;

public class AccountAdapter  extends ArrayAdapter<AccountModel> {
    private final Context context;
    private final List<AccountModel> values;

    public AccountAdapter(Context context, List<AccountModel> values)
    {
        super(context, R.layout.template_account_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountModel thisModel=values.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.template_account_list_item, parent, false);
        TextView accountTextView = (TextView) rowView.findViewById(R.id.accountId);
        TextView certLocationTextView = (TextView) rowView.findViewById(R.id.certLocation);

        accountTextView.setText(thisModel.getAccountId());
        certLocationTextView.setText(thisModel.getCertLocation());


        return rowView;
    }
}
