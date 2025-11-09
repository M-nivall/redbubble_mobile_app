package com.example.Varsani.utils;

public class Urls {


   public static String ipAddress = "https://880c3bea183e.ngrok-free.app/redbubble/";
    //public static String ipAddress = "http://192.168.198.133/redbubble/";

    private static final String ROOT_URL =ipAddress+ "android_files/";
    public static final String ROOT_URL_IMAGES =ipAddress+"upload_products/" ;

    public static  final String URL_PRINT=ipAddress+"print_pdf.php";
    public static  final String UEL_FEEDBACK=ROOT_URL+"client/get_feedback.php";
    public static  final String UEL_FEEDBACK_SEND=ROOT_URL+"client/send_feedback.php";

   public static  final String ROOT_URL_UPLOADS=ROOT_URL+"client/uploads/";
   public static final String ROOT_URL_BRAND_DESIGNS =ROOT_URL+"upload_designs/";
   public static final String URL_DESIGN_ITEMS=ROOT_URL + "client/design_items.php";
   public static final String URL_APPROVE_DESIGN=ROOT_URL + "client/approve_design.php";
   public static final String URL_REJECT_DESIGN=ROOT_URL + "client/reject_design.php";
   public static final String URL_MARK_SERVICE_COMPLETE=ROOT_URL + "client/confirm_complete.php";


    public static  final String UEL_STAFF_SEND_FEEDBACK=ROOT_URL+"client/staff_sendfeedback.php";
    public static  final String UEL_STAFF_FEEDBACK=ROOT_URL+"client/getstafffeedback.php";

     //  products
    public static final String URL_GET_PRODUCTS=ROOT_URL + "client/products.php";
    public static final String URL_ADD_CART=ROOT_URL + "client/add_to_cart.php";
    public static final String URL_GET_CART=ROOT_URL + "client/cart.php";
    public static final String URL_UPDATE_CART=ROOT_URL + "client/car_update.php";
    public static final String URL_REMOVE_ITEM=ROOT_URL + "client/cart_remove.php";

    //services
    public static final String URL_GET_SERVICES=ROOT_URL + "client/services.php";
    public static final String URL_ADD_CART2=ROOT_URL + "client/add_to_cart2.php";
    public static final String URL_GET_CART2=ROOT_URL + "client/cart2.php";
    public static final String URL_GET_ITEMS=ROOT_URL + "client/items.php";
    public static final String URL_GET_STOCK_ITEMS=ROOT_URL + "stock_mrg/items.php";
    public static final String URL_REMOVE_BOOKING=ROOT_URL + "client/booking_remove.php";
    public static final String URL_SUBMIT_REQUEST = ROOT_URL+"client/submit_request.php";

    // shipping
    public static final String URL_GET_COUNTIES=ROOT_URL + "client/counties.php";
    public static final String URL_GET_TOWNS=ROOT_URL + "client/towns.php";
    public static final String URL_DELIVERY_DETAILS=ROOT_URL + "client/delivery_details.php";

    // checkout
    public static final String URL_GET_CHECKOUT_TOTAL=ROOT_URL + "client/checkout_cost.php";
    // user
    public static final String URL_REG = ROOT_URL +"client/register.php";
    public static final String URL_LOGIN= ROOT_URL+"client/login.php";
    public static final String URL_RESET = ROOT_URL + "client/forgotpass.php";
    public static final String URL_RESET2 = ROOT_URL + "client/resetpass.php";
//    SUPPLIERS
    public static final String URL_REG_SUPPLIERS= ROOT_URL+"supplier/reg_supplier.php";
    public static final String URL_REQUEST_STOCK=ROOT_URL + "stock_mrg/send_requests.php";
    public static final String URL_MY_REQUESTS= ROOT_URL+"supplier/my_requests.php";
    public static final String URL_ACCEPT= ROOT_URL+"supplier/approve_items.php";
   // orders
    public static final String URL_SUBMIT_ORDER = ROOT_URL+"client/submit_order.php";
    public static final String URL_GET_ORDERS= ROOT_URL+"client/order_history.php";
    public static final String URL_GET_ORDER_ITEMS= ROOT_URL+"client/order_items.php";
    public static final String URL_GET_ORDER_ITEMS2= ROOT_URL+"client/order_items2.php";
    public static final String URL_MARK_DELIVERED= ROOT_URL+"client/mark_delivered.php";
    public static final String URL_MARK_REJECTED= ROOT_URL+"client/mark_rejected.php";
    public static final String URL_MARK_COMPLETE= ROOT_URL+"client/mark_completed.php";
    public static final String URL_CONFIRM_DELIVERED = ROOT_URL+"client/confirm_delivered.php";

    //invoices
    public static final String URL_GET_INVOICE= ROOT_URL+"client/invoice_history.php";
    public static final String URL_SUBMIT_INVOICE= ROOT_URL+"client/submit_invoice.php";
    //Clients
    public static final String URL_MY_BOOKINGS= ROOT_URL+"client/my_bookings.php";

    //Staff
    public static final String URL_STAFF_LOGIN=ROOT_URL + "staff_login.php";
    //Finance
    public static final String URL_NEW_ORDERS=ROOT_URL + "finance/new_orders.php";
    public static final String URL_GET_CLIENT_ITEMS=ROOT_URL + "client_item.php";
    public static final String URL_GET_APPROVE_ORDERS=ROOT_URL + "finance/approve_order.php";
    public static final String URL_APPROVED_ORDERS=ROOT_URL + "finance/approved_orders.php";
    public static final String URL_NEW_SERV_PAYMENTS=ROOT_URL + "finance/new_serv_payments.php";
    public static final String URL_APPROVE_SERV_PAYMENTS=ROOT_URL + "finance/approve_serv_payments.php";
    public static final String URL_APPROVED_SERV_PAYMENTS=ROOT_URL + "finance/approved_serv_payments.php";
    public static final String URL_SUPPLY_PAYMENTS=ROOT_URL + "finance/supply_payments.php";
    public static final String URL_SUPPLY_PAYMENTS2=ROOT_URL + "finance/supply_payments2.php";
    public static final String URL_PAY_SUPPLIER=ROOT_URL + "finance/pay_supplier.php";

    //shipping mrg
    public static final String URL_ORDERS_TO_SHIP=ROOT_URL + "ship_mrg/orders_to_ship.php";
    public static final String URL_GET_DRIVERS=ROOT_URL + "ship_mrg/get_drivers.php";
    public static final String URL_ASSIGN_DRIVER=ROOT_URL + "ship_mrg/assign_driver.php";
    public static final String URL_SHIP_ORDER=ROOT_URL + "ship_mrg/ship_order.php";
    public static final String URL_SHIPPING_ORDERS=ROOT_URL + "ship_mrg/shipping_orders.php";
    public static final String URL_APPROVE_TENDER=ROOT_URL + "ship_mrg/approve_tender.php";


    //Service   Manager
    public static final String URL_QUOTATION_REQUEST=ROOT_URL + "serv_mrg/quot_requests.php";
    public static final String URL_QUOTATION_ITEMS=ROOT_URL + "quot_items.php";
    public static final String URL_GET_TECHNICIANS=ROOT_URL + "serv_mrg/get_technicians.php";
    public static final String URL_ASSIGN_TECH=ROOT_URL + "serv_mrg/assign_tech.php";
    public static final String URL_SUBMIT_SERVICE_FEE=ROOT_URL + "serv_mrg/approve_booking.php";
    public static final String URL_PENDING_ALLOCATION=ROOT_URL + "serv_mrg/pending_allocation.php";
    public static final String URL_COMPLETED_DESIGNS=ROOT_URL + "serv_mrg/completed_designs.php";
    public static final String URL_ASSIGN_DESIGNER=ROOT_URL + "serv_mrg/assign_designer.php";
    public static final String URL_ASSIGN_TECHNICIAN=ROOT_URL + "serv_mrg/assign_tech.php";
    public static final String URL_GET_DESIGNER=ROOT_URL + "serv_mrg/get_designer.php";
    public static final String URL_GET_TECH=ROOT_URL + "serv_mrg/get_technicians.php";
    public static final String URL_SERVICE_COMPLETED=ROOT_URL + "serv_mrg/completed_services.php";
    public static final String URL_GET_COMPLETION=ROOT_URL + "serv_mrg/pending_confirm.php";
    public static final String URL_CONFIRM_COMPLETE=ROOT_URL + "serv_mrg/confirm_complete.php";


    //technician
    public static final String URL_GET_ASSIGNED_SITES=ROOT_URL + "technician/assigned_orders.php";
    public static final String URL_GET_ASSIGNED_SERVICES=ROOT_URL + "technician/assigned_services.php";
    public static final String URL_SEND_QUOTATION=ROOT_URL + "technician/send_quotation.php";
    public static final String URL_START_WORK=ROOT_URL + "technician/start_work.php";
    public static final String URL_CONFIRM_COMPLETION=ROOT_URL + "technician/confirm_completion.php";

    // Driver

    public static final String URL_GET_ASSIGNED_ORDERS=ROOT_URL + "driver/assigned_orders.php";
    public static final String URL_ARRIVED_ORDERS=ROOT_URL + "driver/arrived_orders.php";
    public static final String URL_DELIVERED_ORDERS=ROOT_URL + "driver/delivered_orders.php";
    public static final String URL_DELIVER=ROOT_URL + "driver/deliver.php";
    //Store mrg
    public static final String URL_GET_STOCK=ROOT_URL + "stock_mrg/stock.php";
    public static final String URL_ADD_STOCK=ROOT_URL + "stock_mrg/add_stock.php";
    public static final String URL_SUPPLIER=ROOT_URL + "stock_mrg/suppliers.php";
    public static final String URL_SEND_REQUEST=ROOT_URL + "stock_mrg/send_requests.php";
    public static final String URL_REQUESTS=ROOT_URL + "stock_mrg/request.php";
    public static final String URL_REQUESTMATERIALS=ROOT_URL + "stock_mrg/material_request.php";
    public static final String URL_APPROVE_MATERIALS=ROOT_URL + "ship_mrg/approve_materials.php";
    public static final String URL_GET_TOOLS=ROOT_URL + "stock_mrg/tools.php";
    //clients
    public static final String URL_SERVICE_DETAILS=ROOT_URL + "client/service_details.php";
    public static final String URL_SUBMIT_BOOKING = ROOT_URL+"client/submit_booking.php";
    //Designer
    public static final String URL_ASSIGNED_DESIGNS=ROOT_URL + "designer/new_designs.php";
    public static final String URL_UPLOAD_DESIGN = ROOT_URL + "designer/upload_design.php/";
    //Tech
    public static final String URL_ASSIGNED_SERVICES=ROOT_URL + "technician/assigned_services.php";
    public static final String URL_PROCEED_SERVICES=ROOT_URL + "technician/in_progress.php";
    public static final String URL_COMPLETE_WORK=ROOT_URL + "technician/complete_work.php";
    public static final String URL_MARK_COMPLETED=ROOT_URL + "technician/confirm_complete.php";

}
