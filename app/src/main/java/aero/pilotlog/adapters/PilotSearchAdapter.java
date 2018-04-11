package aero.pilotlog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.Pilot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuc.dd on 6/29/2017.
 */

public class PilotSearchAdapter extends ArrayAdapter<Pilot> {
    Context context;
    int resource, textViewResourceId;
    List<Pilot> items, tempItems, suggestions;

    public PilotSearchAdapter(Context context, int resource, int textViewResourceId, List<Pilot> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Pilot>(items); // this makes the difference.
        suggestions = new ArrayList<Pilot>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_pilot_search, parent, false);
        }
        Pilot pilot = items.get(position);
        if (pilot != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_pilot_name);
            if (lblName != null)
                lblName.setText(pilot.getPilotName());
            TextView lblEmployee = (TextView) view.findViewById(R.id.lbl_employee_id);
            if (lblEmployee != null) {
                lblEmployee.setText(pilot.getPilotRef() != null ? pilot.getPilotRef() : "");
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Pilot) resultValue).getPilotName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Pilot pilot : tempItems) {
                    if (pilot.getPilotName().toLowerCase().contains(constraint.toString().toLowerCase())
                            || ( pilot.getPilotRef()!=null && pilot.getPilotRef().toLowerCase().contains(constraint.toString().toLowerCase()))) {
                        suggestions.add(pilot);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Pilot> pilotList = (ArrayList<Pilot>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Pilot pilot : pilotList) {
                    add(pilot);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
