package com.example.fit.ui.agua;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.fit.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;

public class Calendar extends Fragment {

    private MaterialCalendarView calendarView;
    private HashSet<CalendarDay> datesWithRecords = new HashSet<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);

        // Aqui você buscara as datas no banco e adicionará ao datesWithRecords
        // Dummy data: substitua isso pela busca no seu banco
        datesWithRecords.add(CalendarDay.from(2023, 4, 3)); // ano, mês (iniciando do 0), dia

        // Aplicar o decorador
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_event_indicator); // Substitua pelo seu drawable
        calendarView.addDecorators(new EventDecorator(drawable, datesWithRecords));

        return view;
    }

    // Decorator inner class
    public class EventDecorator implements DayViewDecorator {
        private final Drawable highlightDrawable;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(Drawable highlight, HashSet<CalendarDay> dates) {
            this.highlightDrawable = highlight;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
        }
    }
}

