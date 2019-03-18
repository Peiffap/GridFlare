package epl.students.programmers.gridflare.tools;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import epl.students.programmers.gridflare.R;

public class Adapter_Scan_information extends RecyclerView.Adapter<Adapter_Scan_information.MyViewHolder>{

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView lv_strength;
        TextView lv_ping;
        TextView lv_lost;
        TextView lv_dl;
        TextView lv_date;
        TextView lv_room;

        MyViewHolder(View itemView) {
            super(itemView);
            lv_strength  = itemView.findViewById(R.id.lv_strength);
            lv_ping = itemView.findViewById(R.id.lv_ping);
            lv_lost = itemView.findViewById(R.id.lv_lost);
            lv_dl = itemView.findViewById(R.id.lv_dl);
            lv_date = itemView.findViewById(R.id.lv_date);
            lv_room = itemView.findViewById(R.id.lv_room);

        }

        void display(Scan_information current_scan){
            assert current_scan != null;
            lv_strength.setText(String.valueOf(current_scan.getStrength()));
            lv_ping.setText(String.valueOf(current_scan.getPing()));
            lv_lost.setText(String.valueOf(current_scan.getProportionOfLost()));
            lv_dl.setText(String.valueOf(current_scan.getDl()));
            lv_date.setText(current_scan.getDate().toString());
            lv_room.setText(current_scan.getRoom());

        }
    }

    private ArrayList<Scan_information> historic;

    public Adapter_Scan_information(ArrayList<Scan_information> historic){
        this.historic = historic;
    }


    @NonNull
    @Override
    public Adapter_Scan_information.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listview_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Scan_information.MyViewHolder holder, int position) {
        holder.display(historic.get(position));
    }

    @Override
    public int getItemCount() {
        return historic.size();
    }
}
