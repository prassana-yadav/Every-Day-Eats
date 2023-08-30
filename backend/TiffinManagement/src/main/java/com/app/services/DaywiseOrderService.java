package com.app.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.DaywiseOrderDao;
import com.app.dao.OrderDao;
import com.app.dao.TiffinDetailDao;
import com.app.dao.UserAddressDao;
import com.app.dao.UserDao;
import com.app.dtos.ActiveUsers;
import com.app.dtos.AssignDeliveryBoy;
import com.app.dtos.DaywiseOrderDto;
import com.app.dtos.DtoEntityConverter;
import com.app.dtos.OrderTiffinDetailsDto;
import com.app.dtos.UserDto;
import com.app.entities.DaywiseOrder;
import com.app.entities.Order;
import com.app.entities.TiffinDetail;
import com.app.entities.User;
import com.app.entities.UserAddress;

@Service
@Transactional
public class DaywiseOrderService {

	@Autowired
	DaywiseOrderDao daywiseOrderDao;
	@Autowired
	DtoEntityConverter converter;
	@Autowired
	private OrderDao orderdao;
	@Autowired
	private UserDao userdao;
	@Autowired
	private TiffinDetailDao tiffindao;
	@Autowired
	private UserAddressDao userAddressdao;

	public DaywiseOrderDto findByDoId(int doId) {
		DaywiseOrder dayWiseOrder = daywiseOrderDao.findByDoId(doId);
		System.out.println(converter.toDaywiseOrderDto(dayWiseOrder));
		return converter.toDaywiseOrderDto(dayWiseOrder);
	}

	public DaywiseOrderDto addDaywise(DaywiseOrderDto daywiseorderDto) {
		DaywiseOrder daywiseOrder = converter.dayWiseOrderDTOtoDayWiseOrder(daywiseorderDto);
		daywiseOrderDao.save(daywiseOrder);
		return converter.toDaywiseOrderDto(daywiseOrder);
	}

	public List<DaywiseOrderDto> GetALLOrders() {
		List<DaywiseOrder> dayw = daywiseOrderDao.findAll();
		List<DaywiseOrderDto> x = new ArrayList<>();
		for (DaywiseOrder d : dayw) {
			System.out.println(d.getOrder().getUser().getAadharNo());
			x.add(converter.toDaywiseOrderDto(d));
		}
		return x;
	}

	public List<DaywiseOrderDto> addDaywiseOrder() throws ParseException {
		//get Todday orders
		List<Order> orders = orderdao.findAll(); //get all orders 
		List<DaywiseOrderDto> daywisedtolist = new ArrayList<DaywiseOrderDto>();//create list to add today orders
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//create new date for comparision with all date in orders 
		Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
		System.out.println(dateWithoutTime);
		List<DaywiseOrder> oldDois = daywiseOrderDao.findByDateLessThan(dateWithoutTime);//created list to get all old dates excpeting today date
		System.out.println("Before today DOI : "+oldDois);
		daywiseOrderDao.deleteAll(oldDois);	//delete all old daywise order expecting todays
		//In table =today daywise order remainaing
		List<DaywiseOrder> findAll = daywiseOrderDao.findAll();//reamaining order (todays order) store in list
		System.out.println("Todays old doi "+findAll);
		HashSet<DaywiseOrder> allDayWiseOrders=new HashSet<>(findAll);//all todays order set in hashset

		for (Order o : orders) {
			//if any new order arrive in orders then we have to first copmare with today dayWise order 
			//if that order alreday not present in daywise order then we need to add in dayWise order
			Date date = new Date();
			System.out.println(date);
			//date created dfor cheacking today date with all orders 
			if(!(o.getStartDate().compareTo(date)>1)) {
				if (o.getEndDate().compareTo(date) >= 1) {
					//ji order new ahe tyach comp karnyasathi (TodayDayWise ORder )DaywiseOrder cha instance kela ahe
					
					DaywiseOrder dwo=new DaywiseOrder(date, 1, "pending", o,null);
					//ti order jar alreday present nasel(allDayWiseOrders) tr tyala add karanyasathi
					if(!allDayWiseOrders.contains(dwo)) {
						//allDayWiseOrders madhe add kel ahe
						dwo=daywiseOrderDao.save(dwo);
						
						System.out.println("new DOI : "+dwo);
						//Hashset madhe ti order add keli ahe
						allDayWiseOrders.add(dwo);
					}
				}
			}
		}
		//Hashset madhun order gheun convert karun list madhe add kel ahe
		for(DaywiseOrder dayO : allDayWiseOrders ) {
			daywisedtolist.add(converter.toDaywiseOrderDto(dayO));
		}
		
		return daywisedtolist;
	}

	public List<OrderTiffinDetailsDto> Countpending() {
//	long count=daywiseOrderDao.count();
		//Daywise order stored in list
		List<DaywiseOrder> x = daywiseOrderDao.findAll();
		//create HashMap<> to store count of each tiffin pending order
		HashMap<Integer, Integer> count = new HashMap<>();
		for (DaywiseOrder d : x) {
			//cheack pending order in all DayWise Order through status
			System.out.println(d.getStatus());
			if (d.getStatus().equals("pending")) {
				//if status is pending then get Order from DayWiseOrder
				Order o = d.getOrder();
				//get Tiffin Id from getTiffinDetails by using order
				int tiffin_id = o.getTiffinDetails().getTiffinId();
				//put tiffin id and count of that tiffin in hashmap
				//getOrDefault :jar tiffin id (key) map madhe cheack karel ahe ka asel tr value replace hoil nasel tr ji default value ahe ti add hoil
				//first tiffin add kartana map empty asel tyacha count 1 asel default value 0 ahe tyamul + 1 kela ahe count 1 yenyasathi
				count.put(tiffin_id, count.getOrDefault(tiffin_id, 0) + 1);
			}
		}
		//OrderTiffinDetailsDto (tiffin Name ,count ) yachi list create keli ahe
		List<OrderTiffinDetailsDto> list = new ArrayList<OrderTiffinDetailsDto>();
		for (Integer i : count.keySet()) {
			//map madhun keySet gheun itrate karyachi
			//map madhun get(key) count (value) bhetel 
			int c = count.get(i);
			//tiffinDetails find karyachy key varun (tiffin id)
			TiffinDetail t = tiffindao.findByTiffinId(i);
			//dto create kel ahe tiffin Name ,count store kela ahe
			OrderTiffinDetailsDto ot = new OrderTiffinDetailsDto(t.getTiffinName(), c);
			//list madhe add kel ahe
			list.add(ot);
		}
		System.out.println(list);
		return list;
	}

	public List<OrderTiffinDetailsDto> CountDispatched() {
//	long count=daywiseOrderDao.count();
		List<DaywiseOrder> x = daywiseOrderDao.findAll();
		HashMap<Integer, Integer> count = new HashMap<>();

		for (DaywiseOrder d : x) {
			System.out.println(d.getStatus());
			if (d.getStatus().equals("dispatched")) {
				Order o = d.getOrder();
				int tiffin_id = o.getTiffinDetails().getTiffinId();
				count.put(tiffin_id, count.getOrDefault(tiffin_id, 0) + 1);
			}

		}
		List<OrderTiffinDetailsDto> list = new ArrayList<OrderTiffinDetailsDto>();
		for (Integer i : count.keySet()) {
			int c = count.get(i);
			TiffinDetail t = tiffindao.findByTiffinId(i);
			OrderTiffinDetailsDto ot = new OrderTiffinDetailsDto(t.getTiffinName(), c);
			list.add(ot);
		}

		System.out.println(list);

		return list;
	}

	public List<AssignDeliveryBoy> TotaltodaysPendingOrderList() {

		List<DaywiseOrder> daywiseorder = daywiseOrderDao.findAll();
		List<AssignDeliveryBoy> assignd = new ArrayList<AssignDeliveryBoy>();
		for (DaywiseOrder d : daywiseorder) {
			if (d.getStatus().equals("pending")) {
				UserAddress ud = userAddressdao.findByUserId(d.getOrder().getUser().getUserId());

				AssignDeliveryBoy a = new AssignDeliveryBoy(d.getDoId(), d.getOrder().getUser().getUserName(),
						d.getOrder().getOrderId(), ud.getAddressLine(), ud.getDeliveryAddress().getDeliveryArea(),
						ud.getDeliveryAddress().getCity(), ud.getDeliveryAddress().getPinCode());
				System.out.println(a.getDo_id());
				assignd.add(a);
			}

		}
//	System.out.println(assignd);
		return assignd;
	}

	public List<UserDto> getDeliveryBoys() {
		List<User> users = userdao.findAll();
		List<UserDto> deliveryBoys = new ArrayList<>();
		for (User u : users) {
			if (u.getRole().equals("ROLE_DELIVERYBOY")) {
				deliveryBoys.add(converter.toUserDto(u));
			}
		}
		return deliveryBoys;
	}

	public String DispatchOrder(String userId, String do_id) {
		
		int userid = Integer.parseInt(userId);
		int doId = Integer.parseInt(do_id);
		//doID varun dayWiseOrder bhetel
		DaywiseOrder d = daywiseOrderDao.findByDoId(doId);
		//DayWiseOrder madhe setDelivery karyach
		d.setDeliveryBoy(userdao.findByUserId(userid));
		//statuc change to dispatched 
		d.setStatus("dispatched");
		daywiseOrderDao.save(d);
		return "successfully done";
	}

	public List<AssignDeliveryBoy> getDeliveryDetailsforDeliveryBoy(int userId) {
		List<DaywiseOrder> dayorders = daywiseOrderDao.findAll();
		List<AssignDeliveryBoy> dblist = new ArrayList<AssignDeliveryBoy>();
		for (DaywiseOrder d : dayorders) {
			if (d.getDeliveryBoy()!=null && d.getDeliveryBoy().getUserId() == userId && d.getStatus().equals("dispatched")) {
				UserAddress ud = userAddressdao.findByUserId(d.getOrder().getUser().getUserId());

				AssignDeliveryBoy a = new AssignDeliveryBoy(d.getDoId(), d.getOrder().getUser().getUserName(),
						d.getOrder().getOrderId(), ud.getAddressLine(), ud.getDeliveryAddress().getDeliveryArea(),
						ud.getDeliveryAddress().getCity(), ud.getDeliveryAddress().getPinCode());
				System.out.println(a.getDo_id());
				dblist.add(a);
			}
		}
		return dblist;

	}

	public int DispatchedToDelivered(int do_id) {
		DaywiseOrder d = daywiseOrderDao.findByDoId(do_id);
		d.setStatus("Delivered");
		return 1;
	}

	public List<AssignDeliveryBoy> TotaltodaysDispatchedOrders() {

		List<DaywiseOrder> daywiseorder = daywiseOrderDao.findAll();
		List<AssignDeliveryBoy> assignd = new ArrayList<AssignDeliveryBoy>();
//		System.out.println(daywiseorder);
		for (DaywiseOrder d : daywiseorder) {
			if (d.getStatus().equals("dispatched")) {
				UserAddress ud = userAddressdao.findByUserId(d.getOrder().getUser().getUserId());

				AssignDeliveryBoy a = new AssignDeliveryBoy(d.getDoId(), d.getOrder().getUser().getUserName(),
						d.getOrder().getOrderId(), ud.getAddressLine(), ud.getDeliveryAddress().getDeliveryArea(),
						ud.getDeliveryAddress().getCity(), ud.getDeliveryAddress().getPinCode());
				System.out.println(a.getDo_id());
				assignd.add(a);
			}

		}
		System.out.println(assignd);
		return assignd;
	}

	public List<OrderTiffinDetailsDto> CountDelivered() {
//		long count=daywiseOrderDao.count();
		List<DaywiseOrder> x = daywiseOrderDao.findAll();
		HashMap<Integer, Integer> count = new HashMap<>();

		for (DaywiseOrder d : x) {
			System.out.println(d.getStatus());
			if (d.getStatus().equals("Delivered")) {
				Order o = d.getOrder();
				int tiffin_id = o.getTiffinDetails().getTiffinId();
				count.put(tiffin_id, count.getOrDefault(tiffin_id, 0) + 1);
			}

		}
		List<OrderTiffinDetailsDto> list = new ArrayList<OrderTiffinDetailsDto>();
		for (Integer i : count.keySet()) {
			int c = count.get(i);
			TiffinDetail t = tiffindao.findByTiffinId(i);
			OrderTiffinDetailsDto ot = new OrderTiffinDetailsDto(t.getTiffinName(), c);
			list.add(ot);
		}

//			System.out.println(list + "   ssja");

		return list;
	}

	public List<AssignDeliveryBoy> TotaltodaysDeliveredOrders() {
		List<DaywiseOrder> daywiseorder = daywiseOrderDao.findAll();
		List<AssignDeliveryBoy> assignd = new ArrayList<AssignDeliveryBoy>();
		for (DaywiseOrder d : daywiseorder) {
			if (d.getStatus().equals("Delivered")) {
				UserAddress ud = userAddressdao.findByUserId(d.getOrder().getUser().getUserId());

				AssignDeliveryBoy a = new AssignDeliveryBoy(d.getDoId(), d.getOrder().getUser().getUserName(),
						d.getOrder().getOrderId(), ud.getAddressLine(), ud.getDeliveryAddress().getDeliveryArea(),
						ud.getDeliveryAddress().getCity(), ud.getDeliveryAddress().getPinCode());
				System.out.println(a.getDo_id());
				assignd.add(a);
			}

		}
		System.out.println(assignd);
		return assignd;
	}

	public List<ActiveUsers> getAllActiveUsers() {
		List<Order> orders = orderdao.findAll();
		List<ActiveUsers> activeusers = new ArrayList<ActiveUsers>();

		for (Order o : orders) {

			Date date = new Date();
			System.out.println(o.getOrderId() + "" + o.getEndDate().compareTo(date));
			if (o.getEndDate().compareTo(date) >= 0) {

				activeusers.add(new ActiveUsers(o.getUser().getUserId(), o.getUser().getUserName(), o.getStartDate(),
						o.getEndDate(), o.getTotalDays(), o.getTotalAmount(), o.getTiffinDetails().getTiffinId()));

			}
		}
		return activeusers;

	}
}
