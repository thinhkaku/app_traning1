package aero.pilotlog.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.Aircraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuc.dd on 6/29/2017.
 */

public class AircraftSearchAdapter extends ArrayAdapter<Aircraft>{
    Context context;
    int resource, textViewResourceId;
    List<Aircraft> items, tempItems, suggestions;

    public AircraftSearchAdapter(Context context, int resource, int textViewResourceId, List<Aircraft> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Aircraft>(items); // this makes the difference.
        suggestions = new ArrayList<Aircraft>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_aircraft_search, parent, false);
        }
        Aircraft aircraft = items.get(position);
        if (aircraft != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_reference);
            if (lblName != null)
                lblName.setText(aircraft.getReference());
            TextView lblModel = (TextView) view.findViewById(R.id.lbl_model);
            if (lblModel != null) {
                lblModel.setText(aircraft.getModel());
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
            String str = ((Aircraft) resultValue).getRefSearch();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Aircraft aircraft : tempItems) {
                    if (aircraft.getRefSearch().toLowerCase().contains(constraint.toString().toLowerCase())
                            || aircraft.getModel().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(aircraft);
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
            List<Aircraft> filterList = (ArrayList<Aircraft>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Aircraft aircraft : filterList) {
                    add(aircraft);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
