package epl.students.programmers.gridflare.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import epl.students.programmers.gridflare.R;

public class Adapter_Scanne_information extends ArrayAdapter<Scanne_information> {
    public Adapter_Scanne_information(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.listview_items,null);

        Scanne_information current_scan = getItem(position);

        TextView lv_strength  = v.findViewById(R.id.lv_strength);
        TextView lv_ping = v.findViewById(R.id.lv_ping);
        TextView lv_lost = v.findViewById(R.id.lv_lost);
        TextView lv_dl = v.findViewById(R.id.lv_dl);
        TextView lv_date = v.findViewById(R.id.lv_date);
        TextView lv_room = v.findViewById(R.id.lv_room);

        assert current_scan != null;
        lv_strength.setText(String.valueOf(current_scan.getStrength()));
        lv_ping.setText(String.valueOf(current_scan.getPing()));
        lv_lost.setText(String.valueOf(current_scan.getProportionOfLost()));
        lv_dl.setText(String.valueOf(current_scan.getDl()));
        lv_date.setText(current_scan.getDate().toString());
        lv_room.setText(current_scan.getRoom());

        return v;
    }
}