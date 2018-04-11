package aero.pilotlog.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import aero.crewlounge.pilotlog.R;
import aero.pilotlog.databases.entities.Airfield;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuc.dd on 6/29/2017.
 */

public class AirfieldSearchAdapter extends ArrayAdapter<Airfield> {
    Context context;
    int resource, textViewResourceId;
    List<Airfield> items, tempItems, suggestions;

    public AirfieldSearchAdapter(Context context, int resource, int textViewResourceId, List<Airfield> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        try{
            tempItems = new ArrayList<Airfield>(items); // this makes the difference.
            suggestions = new ArrayList<Airfield>();
        }catch (Exception e){

        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_airfield_search, parent, false);
        }
        Airfield airfield = items.get(position);
        if (airfield != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_af_name);
            if (lblName != null)
                lblName.setText(airfield.getAFName());
            TextView lblIcaoIata = (TextView) view.findViewById(R.id.lbl_af_icao_iata);
            if (lblIcaoIata != null) {
                String icao = airfield.getAFICAO();
                String iata = airfield.getAFIATA();
                if (!TextUtils.isEmpty(icao) && !TextUtils.isEmpty(iata)) {
                    lblIcaoIata.setText(icao + " - " + iata);
                } else if (TextUtils.isEmpty(icao)) {
                    lblIcaoIata.setText(iata);
                } else {
                    lblIcaoIata.setText(icao);
                }
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
            String str = ((Airfield) resultValue).getAFName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Airfield airfield : tempItems) {
                    if (airfield.getAFName().toLowerCase().contains(constraint.toString().toLowerCase())
                            || airfield.getAFICAO().toLowerCase().contains(constraint.toString().toLowerCase())
                            || airfield.getAFIATA().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(airfield);
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
            List<Airfield> filterList = (ArrayList<Airfield>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Airfield airfield : filterList) {
                    add(airfield);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
