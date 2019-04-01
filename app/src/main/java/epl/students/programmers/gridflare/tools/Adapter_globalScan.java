package epl.students.programmers.gridflare.tools;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import epl.students.programmers.gridflare.R;

public class Adapter_globalScan extends RecyclerView.Adapter<Adapter_globalScan.MyViewHolder>{

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView lv_strength;
        TextView lv_room;
        ProgressBar progressBar;
        TextView number_of_scans;

        MyViewHolder(View itemView) {
            super(itemView);
            lv_strength  = itemView.findViewById(R.id.lv_strength_global);
            lv_room = itemView.findViewById(R.id.lv_room_global);
            progressBar = itemView.findViewById(R.id.progress_historic_global);
            number_of_scans = itemView.findViewById(R.id.global_scan_number_of_scans);
        }

        void display(Scan_information current_scan){
            assert current_scan != null;
            lv_strength.setText(String.format("%s : %s", String.valueOf(lv_strength.getText()), String.valueOf(current_scan.getStrength() + "%")));
            int prog = current_scan.getStrength();
            lv_room.setText(current_scan.getRoom().toString());
            progressBar.setProgress(prog);
            if(prog > 70)
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            else if(prog > 40)
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            else
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

            if(current_scan.getNumberOfScans() == 0 || current_scan.getNumberOfScans() == 999){
                lv_strength.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
            number_of_scans.setText(""+current_scan.getNumberOfScans());
        }
    }

    private ArrayList<Scan_information> historic;

    public Adapter_globalScan(ArrayList<Scan_information> historic){
        this.historic = historic;
    }

    @NonNull
    @Override
    public Adapter_globalScan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.global_scan_item,parent,false);
        return new Adapter_globalScan.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_globalScan.MyViewHolder holder, int position) {
        holder.display(historic.get(position));
    }

    @Override
    public int getItemCount() {
        return historic.size();
    }
}
