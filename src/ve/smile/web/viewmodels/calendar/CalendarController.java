package ve.smile.web.viewmodels.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

public class CalendarController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	 
    @Wire
    private Calendars calendars;
    @Wire
    private Label dateMes;
    String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

	private DemoCalendarModel calendarModel;
 
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        calendarModel = new DemoCalendarModel(new DemoCalendarData().getCalendarEvents());
        calendars.setModel(this.calendarModel);
        mesString();

    }
    
    public void mesString(){
        Date dateCurrent = calendars.getCurrentDate();
        dateMes.setValue(monthNames[dateCurrent.getMonth()]); 
    }
     
    //control the calendar position
    @Listen("onClick = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
        mesString();
    }
    @Listen("onClick = #next")
    public void gotoNext(){
        calendars.nextPage();
        mesString();
    }
    @Listen("onClick = #prev")
    public void gotoPrev(){
        calendars.previousPage();
        mesString();
    }

    @Listen("onClick = #pageWeek")
    public void changeToWeek(){
        calendars.setMold("default");
        calendars.setDays(7);
    }
    @Listen("onClick = #pageMonth")
    public void changeToYear(){
        calendars.setMold("month");
    }
 
    //listen to the calendar-create and edit of a event data
    @Listen("onEventEdit = #calendars")
    public void createEvent(CalendarsEvent event) {
        
    	System.out.println(event.getCalendarEvent().getTitle());
    }
}
