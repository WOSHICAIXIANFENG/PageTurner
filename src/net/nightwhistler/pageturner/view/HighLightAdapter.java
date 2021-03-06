/*
 * Copyright (C) 2011 Alex Kuiper
 * 
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */

package net.nightwhistler.pageturner.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.nightwhistler.pageturner.PlatformUtil;
import net.nightwhistler.pageturner.R;
import net.nightwhistler.pageturner.dto.HighLight;
import net.nightwhistler.pageturner.tasks.SearchTextTask;
import net.nightwhistler.pageturner.view.bookview.BookView;

import java.util.List;

/**
 * ListAdapter class for search results.
 * 
 * @author Alex Kuiper
 *
 */
public class HighLightAdapter extends ArrayAdapter<HighLight> implements
	DialogInterface.OnClickListener {

	private List<HighLight> highLights;
	private BookView bookView;

	public HighLightAdapter(Context context, BookView bookView,
                            List<HighLight> highLights) {
		super(context, R.id.deviceName, highLights);
		this.highLights = highLights;
		this.bookView = bookView;	
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		HighLight highLight = this.highLights.get(which);
        bookView.navigateTo( highLight.getIndex(), highLight.getStart() );
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView;

		if ( convertView == null ) {
			LayoutInflater inflater = PlatformUtil.getLayoutInflater(getContext());

			rowView = inflater.inflate(R.layout.progress_row, parent, false);
		} else {
			rowView = convertView;
		}

		TextView deviceView = (TextView) rowView.findViewById(R.id.deviceName);
		TextView percentageView = (TextView) rowView.findViewById(R.id.timeStamp );
				
		if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
			deviceView.setTextColor( Color.BLACK );
			percentageView.setTextColor( Color.BLACK );			
		}
			
		HighLight highLight = highLights.get(position);

		deviceView.setText( highLight.getDisplayText() );

        int progressPercentage = bookView.getPercentageFor(highLight.getIndex(), highLight.getStart() );
        int pageNumber = bookView.getPageNumberFor(highLight.getIndex(), highLight.getStart() );
        int totalPages = bookView.getTotalNumberOfPages();

        Log.d( "HighLightAdapter", "Highlight index=" + highLight.getIndex() + " start=" + highLight.getStart()
                + " percentage=" + progressPercentage + " pageNumber=" + pageNumber);

        String text = progressPercentage + "%";

        if ( pageNumber != -1 ) {
            text = String.format( getContext().getString(R.string.page_number_of),
                    pageNumber, totalPages ) + " (" + progressPercentage + "%)";
        }

        if ( highLight.getTextNote() != null && highLight.getTextNote().trim().length() > 0 ) {
            text += ": " + highLight.getTextNote();
        }

        percentageView.setText( text );

		return rowView;

	}
}
