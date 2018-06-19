package devs.mulham.horizontalcalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * custom adapter for {@link HorizontalCalendarView HorizontalCalendarView}
 *
 * @author Mulham-Raee
 * @version 1.1
 * See {devs.mulham.horizontalcalendar.R.layout#item_calendar} Calendar CustomItem Layout
 */
class HorizontalCalendarAdapter extends RecyclerView.Adapter<HorizontalCalendarAdapter.DayViewHolder> {

    private final Context context;
    private ArrayList<Date> datesList;
    private int widthCell;
    private HorizontalCalendar horizontalCalendar;
    private int numberOfDates;
    private HorizontalCalendarView horizontalCalendarView;
    private int selectedItemPosition = -1;

    HorizontalCalendarAdapter(HorizontalCalendarView horizontalCalendarView, ArrayList<Date> datesList) {
        this.horizontalCalendarView = horizontalCalendarView;
        this.context = horizontalCalendarView.getContext();
        this.datesList = datesList;
        this.horizontalCalendar = horizontalCalendarView.getHorizontalCalendar();
        this.numberOfDates = horizontalCalendar.getNumberOfDatesOnScreen();
        calculateCellWidth();

    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar, viewGroup, false);

        convertView.setMinimumWidth(widthCell);

        final DayViewHolder holder = new DayViewHolder(convertView);
        holder.selectionView.setBackgroundColor(horizontalCalendar.getSelectorColor());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = datesList.get(holder.getAdapterPosition());

                if (!date.before(horizontalCalendar.getDateStartCalendar())
                        && !date.after(horizontalCalendar.getDateEndCalendar())) {
                    horizontalCalendarView.setSmoothScrollSpeed(HorizontalLayoutManager.SPEED_FAST);
                    horizontalCalendar.centerCalendarToPosition(holder.getAdapterPosition());
                    HorizontalCalendarListener calendarListener = horizontalCalendar.getCalendarListener();
                    if ((calendarListener != null) && !date.before(horizontalCalendar.getDateStartCalendar())
                            && !date.after(horizontalCalendar.getDateEndCalendar())) {
                         calendarListener.onDateSelected(date, holder.getAdapterPosition());
                         selectedItemPosition = holder.getAdapterPosition();

                        /*horizontalCalendar.post(new Runnable() {
                            @Override
                            public void run() {
                               notifyDataSetChanged();
                            }
                        });*/
                    }
                }
            }
        });

        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Date date = datesList.get(holder.getAdapterPosition());
                HorizontalCalendarListener calendarListener = horizontalCalendar.getCalendarListener();
                if ((calendarListener != null) && !date.before(horizontalCalendar.getDateStartCalendar())
                        && !date.after(horizontalCalendar.getDateEndCalendar())) {
                    return calendarListener.onDateLongClicked(date, holder.getAdapterPosition());
                }
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Date day = datesList.get(position);
        if(selectedItemPosition==-1)
            selectedItemPosition = horizontalCalendar.getDefaultDatePosition();
        else
            selectedItemPosition = horizontalCalendar.getSelectedDatePosition();
       //new logic for auto selecting the date that is in center
        /*if(selectedItemPosition>-1) {
            Date date = datesList.get(selectedItemPosition);

            HorizontalCalendarListener calendarListener = horizontalCalendar.getCalendarListener();
            if ((calendarListener != null) && !date.before(horizontalCalendar.getDateStartCalendar())
                    && !date.after(horizontalCalendar.getDateEndCalendar())) {
                calendarListener.onDateSelected(date, holder.getAdapterPosition());

            }
        }*/

        // Selected Day
        /*if (position == selectedItemPosition) {
            holder.txtDayNumber.setTextColor(horizontalCalendar.getTextColorSelected());
            holder.txtMonthName.setTextColor(horizontalCalendar.getTextColorSelected());
            holder.txtDayName.setTextColor(Color.BLACK);
            holder.txtDayName.setTypeface( holder.txtDayName.getTypeface(), Typeface.BOLD);
            holder.txtDayNumber.setTypeface( holder.txtDayNumber.getTypeface(), Typeface.BOLD);
            holder.layoutBackground.setBackgroundColor(horizontalCalendar.getSelectedDateBackground());
            holder.selectionView.setVisibility(View.GONE);

        }*/
        // Unselected Days
      //  else {
            holder.txtDayNumber.setTextColor(horizontalCalendar.getTextColorNormal());
            holder.txtMonthName.setTextColor(horizontalCalendar.getTextColorNormal());
            holder.txtDayName.setTextColor(Color.BLACK);
            holder.txtDayName.setTypeface( holder.txtDayName.getTypeface(), Typeface.BOLD);
            holder.txtDayNumber.setTypeface( holder.txtDayNumber.getTypeface(), Typeface.BOLD);
            holder.layoutBackground.setBackgroundColor(Color.TRANSPARENT);
            holder.selectionView.setVisibility(View.GONE);
     //   }

        holder.txtDayNumber.setText(DateFormat.format(horizontalCalendar.getFormatDayNumber(), day).toString());
        holder.txtDayNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                horizontalCalendar.getTextSizeDayNumber());

        if (horizontalCalendar.isShowDayName()) {
            holder.txtDayName.setText(DateFormat.format(horizontalCalendar.getFormatDayName(), day).toString());
            holder.txtDayName.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    horizontalCalendar.getTextSizeDayName());
        } else {
            holder.txtDayName.setVisibility(View.GONE);
        }

        if (horizontalCalendar.isShowMonthName()) {
            holder.txtMonthName.setText(DateFormat.format(horizontalCalendar.getFormatMonth(), day).toString());
            holder.txtMonthName.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    horizontalCalendar.getTextSizeMonthName());
        } else {
            holder.txtMonthName.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }

    public Date getItem(int position) {
        return datesList.get(position);
    }

    /**
     * calculate each item width depends on {@link HorizontalCalendar#numberOfDatesOnScreen}
     */
    private void calculateCellWidth() {

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        int widthScreen;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);

            widthScreen = size.x;
        } else {
            widthScreen = display.getWidth();
        }

        widthCell = widthScreen / numberOfDates;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayNumber;
        TextView txtDayName;
        TextView txtMonthName;
        View selectionView;
        View layoutBackground;
        View rootView;

        public DayViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            txtDayNumber = (TextView) rootView.findViewById(R.id.dayNumber);
            txtDayName = (TextView) rootView.findViewById(R.id.dayName);
            txtMonthName = (TextView) rootView.findViewById(R.id.monthName);
            layoutBackground = rootView.findViewById(R.id.layoutBackground);
            selectionView = rootView.findViewById(R.id.selection_view);
        }
    }
}
