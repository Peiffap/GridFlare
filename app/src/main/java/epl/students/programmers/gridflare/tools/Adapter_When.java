package epl.students.programmers.gridflare.tools;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import epl.students.programmers.gridflare.R;

public class Adapter_When extends RecyclerView.Adapter<Adapter_When.MyViewHolder>{

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date_when_scan;

        MyViewHolder(View itemView) {
            super(itemView);
            date_when_scan = itemView.findViewById(R.id.lv_room_rooms_item);

        }

        void display(GlobalScan globalScan){
            assert globalScan != null;
            date_when_scan.setText(globalScan.getDate().toString());

        }
    }

    private ArrayList<GlobalScan> historic;

    public Adapter_When(ArrayList<GlobalScan> historic){
        this.historic = historic;
    }

    @NonNull
    @Override
    public Adapter_When.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rooms_item,parent,false);
        return new Adapter_When.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_When.MyViewHolder holder, int position) {
        holder.display(historic.get(position));
    }

    @Override
    public int getItemCount() {
        return historic.size();
    }
}
