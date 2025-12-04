package com.example.examen_macia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ActivityAdapter extends BaseAdapter {

    private List<Activitat> activitats;
    private LayoutInflater inflater;

    public ActivityAdapter(Context context, List<Activitat> activitats) {
        this.activitats = activitats;
        this.inflater = LayoutInflater.from(context);
    }

    public void updateActivities(List<Activitat> newActivitats) {
        this.activitats = newActivitats;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return activitats != null ? activitats.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return activitats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_activity, parent, false);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.item_icon);
            holder.nom = convertView.findViewById(R.id.item_nom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Activitat activitat = activitats.get(position);
        holder.icon.setImageResource(activitat.getIconResId());
        holder.nom.setText(activitat.getNom());

        return convertView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView nom;
    }
}
