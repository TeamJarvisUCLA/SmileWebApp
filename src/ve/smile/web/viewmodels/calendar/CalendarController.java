package ve.smile.web.viewmodels.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.zkoss.bind.BindUtils;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

import ve.smile.dto.EventoPlanificado;

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
 
	
	public EventoPlanificado findEventoPlanList(String id){
			
		EventoPlanificado resultado = null;
		int idEventoPlan = Integer.parseInt(id);
		
		List<EventoPlanificado> eventosPlan = new DemoCalendarData().getEventosPlan();
		
		for(Iterator<EventoPlanificado> i = eventosPlan.iterator(); i.hasNext(); ) {
		    EventoPlanificado item = i.next();
		    if  (item.getIdEventoPlanificado().equals(idEventoPlan)) {
		    	resultado = item;
		    	break;
		    }
			
		}
		return resultado;
	}
    
    //listen to the calendar-create and edit of a event data
    @Listen("onEventEdit = #calendars")
    public void createEvent(CalendarsEvent event) {

		EventoPlanificado eventoplan = findEventoPlanList(event.getCalendarEvent().getTitle());
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("eventoPlan", eventoplan);
    	
    	BindUtils.postGlobalCommand(null,null,"refreshDetalleAlbum",args);
    }
}
