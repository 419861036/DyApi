//package com.exe.web.debug;
//
//import com.exe.base.Cmd;
//import com.exe.base.CmdHandle;
//import com.exe.base.vo.CmdVo;
//import com.sun.jdi.*;
//import com.sun.jdi.connect.AttachingConnector;
//import com.sun.jdi.connect.Connector;
//import com.sun.jdi.event.*;
//import com.sun.jdi.request.BreakpointRequest;
//import com.sun.jdi.request.EventRequest;
//import com.sun.jdi.request.EventRequestManager;
//import com.sun.tools.jdi.LocationImpl;
//import com.sun.tools.jdi.SocketAttachingConnector;
//import kkd.common.util.StringUtil;
//import sun.java2d.loops.GraphicsPrimitive;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DebuggerCmd extends Cmd implements  Runnable{
//
//    public static final String HOST = "hostname";
//
//    public static final String PORT = "port";
//
//    private VirtualMachine vm;
//
//    private EventQueue eventQueue;
//
//    private EventRequestManager eventRequestManager;
//
//    private Process process;
//
//    private EventSet eventSet;
//
//    private Event event;
//
//    private boolean vmExit = false;
//    private boolean cancelData = false;
//
//    /**
//     * 连接调试服务
//     * @param ip
//     * @param port
//     */
//    public void init(String ip,int port){
//        // 一、取得连接器
//        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
//        List<AttachingConnector> connectors = vmm.attachingConnectors();
//        SocketAttachingConnector sac = null;
//        for (AttachingConnector ac : connectors) {
//            if (ac instanceof SocketAttachingConnector) {
//                sac = (SocketAttachingConnector) ac;
//                break;
//            }
//        }
//        if (sac == null) {
//            System.out.println("JDI error");
//            return;
//        }
//
//        // 二、连接到远程虚拟器
//        Map<String, Connector.Argument> arguments = sac.defaultArguments();
//        Connector.Argument hostArg = (Connector.Argument) arguments.get(HOST);
//        Connector.Argument portArg = (Connector.Argument) arguments.get(PORT);
//
//        hostArg.setValue(ip);
//        portArg.setValue(String.valueOf(port));
//        try {
//            vm = sac.attach(arguments);
//            process = vm.process();
//            eventRequestManager = vm.eventRequestManager();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//    /**
//     * 取消断点
//     */
//    public void cancelBreakPoint(String key,int lineNum){
//        // 三、取得要关注的类和方法
//        Class<?> cls=CmdHandle.getCmd(key);
//        List<ReferenceType> classesByName = vm.classesByName(cls.getName());
//        if (classesByName == null || classesByName.size() == 0) {
//            System.out.println("No class found");
//            return;
//        }
//        // 四、注册监听
//        vm.setDebugTraceMode(VirtualMachine.TRACE_EVENTS);
//        vm.resume();
//        try {
//            List<Location> locations = classesByName.get(0).locationsOfLine(lineNum);
//            BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(locations.get(0));
//            breakpointRequest.disable();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 设置断点
//     */
//    public void setBreakPoint(String key,int lineNum){
//        // 三、取得要关注的类和方法
//        Class<?> cls=CmdHandle.getCmd(key);
//        List<ReferenceType> classesByName = vm.classesByName(cls.getName());
//        if (classesByName == null || classesByName.size() == 0) {
//            System.out.println("No class found");
//            return;
//        }
//        // 四、注册监听
//        vm.setDebugTraceMode(VirtualMachine.TRACE_EVENTS);
//        vm.resume();
//        try {
//            List<Location> locations = classesByName.get(0).locationsOfLine(lineNum);
//            BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(locations.get(0));
//            breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
//            breakpointRequest.enable();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 取消获取数据
//     */
//    public void cancelData(){
//        cancelData=true;
//    }
//
//
//    /**
//     * 停止
//     */
//    public void stop(){
//        vmExit=true;
//        vm.dispose();
//    }
//
//
//    public static void main(String[] args) throws Exception {
//        DebuggerCmd debugger=new DebuggerCmd();
//        debugger.init("127.0.0.1",8800);
//
//        debugger.setBreakPoint("com.exe.bo.debug.Test",34);
//
//        Value s=debugger.getData("com.exe.bo.debug.Test",34,"i");
//        debugger.cancelData();
//        System.out.println("sssssssss:"+s);
//        debugger.cancelBreakPoint("com.exe.bo.debug.Test",34);
//        debugger.stop();
//
//    }
//
//    private  void test() throws Exception {
//        // 三、取得要关注的类和方法
//        List<ReferenceType> classesByName = vm.classesByName("com.exe.bo.debug.Test");
//        if (classesByName == null || classesByName.size() == 0) {
//            System.out.println("No class found");
//            return;
//        }
//        ReferenceType rt = classesByName.get(0);
//        List<Method> methodsByName = rt.methodsByName("printHello");
//        if (methodsByName == null || methodsByName.size() == 0) {
//            System.out.println("No method found");
//            return;
//        }
//        Method method = methodsByName.get(0);
//
//        // 四、注册监听
//        vm.setDebugTraceMode(VirtualMachine.TRACE_EVENTS);
//        vm.resume();
//        List<Location> locations = classesByName.get(0).locationsOfLine(34);
//        BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(locations.get(0));
//        breakpointRequest.enable();
//
////        MethodEntryRequest methodEntryRequest = eventRequestManager.createMethodEntryRequest();
////        methodEntryRequest.addClassFilter(rt);
////        methodEntryRequest.setSuspendPolicy(EventRequest.SUSPEND_NONE);
////        // methodEntryRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
////
////        methodEntryRequest.enable();
////
////        BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(method.location());
////        breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
////        // breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
////        breakpointRequest.enable();
//
//        // ClassPrepareRequest classPrepareRequest = eventRequestManager.createClassPrepareRequest();
//        // classPrepareRequest.addClassFilter("test.Test");
//        // classPrepareRequest.addCountFilter(1);
//        // classPrepareRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
//        // classPrepareRequest.enable();
//
//    }
//
//
//
//    public void resume(){
//        eventSet.resume();
//    }
//
//    private  void eventLoop() throws Exception {
//        eventQueue = vm.eventQueue();
//        while (true) {
//            if (vmExit == true) {
//                break;
//            }
//            eventSet = eventQueue.remove();
//            EventIterator eventIterator = eventSet.eventIterator();
//            while (eventIterator.hasNext()) {
//                Event event = (Event) eventIterator.next();
//                execute(event);
//            }
//        }
//    }
//
//    public Value getData(String className,int lineNum,String var){
//        try{
//            cancelData=false;
//            eventQueue = vm.eventQueue();
//            while (true) {
//                if (vmExit == true||cancelData) {
//                    break;
//                }
//                System.out.println("----------111111");
//                eventSet = eventQueue.remove();
//                EventIterator eventIterator = eventSet.eventIterator();
//                while (eventIterator.hasNext()) {
//                    System.out.println("----------2222222222");
//                    Event event = (Event) eventIterator.next();
//                    if(event instanceof BreakpointEvent){
//                        LocationImpl loc= (LocationImpl) ((BreakpointEvent) event).location();
//                        int lineNum1=loc.lineNumber();
//                        String className1=loc.declaringType().name();
//                        System.out.println("----------3333333"+className1+":"+lineNum1);
////                        if(className.equalsIgnoreCase(className1)  && lineNum==lineNum1){
//                            System.out.println("----------44444444444444");
//                            BreakpointEvent breakpointEvent = (BreakpointEvent) event;
//                            System.out.println("----------"+breakpointEvent);
//                            ThreadReference threadReference = breakpointEvent.thread();
//                            StackFrame stackFrame = threadReference.frame(0);
//                            stackFrame.visibleVariables();
//                            LocalVariable localVariable = stackFrame.visibleVariableByName(var);
//                            Value value = stackFrame.getValue(localVariable);
////                        String date = ((StringReference) value).value();
//                            eventSet.resume();
//                            return value;
////                        }
//
//                    }
//                    eventSet.resume();
//                }
//                Thread.sleep(1000);
//            }
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private  void execute(Event event) throws Exception {
//        this.event=event;
//        if (event instanceof VMStartEvent) {
//            System.out.println("VM started");
//            eventSet.resume();
//        } else if (event instanceof BreakpointEvent) {
//            System.out.println("Reach Method printHello of test.Test");
////
////            BreakpointEvent breakpointEvent = (BreakpointEvent) event;
////            ThreadReference threadReference = breakpointEvent.thread();
////            StackFrame stackFrame = threadReference.frame(0);
////
////            stackFrame.visibleVariables();
////
////            // 获取date变量
////            LocalVariable localVariable = stackFrame.visibleVariableByName("date");
////            Value value = stackFrame.getValue(localVariable);
////            String date = ((StringReference) value).value();
////
////            LocalVariable localVariable1 = stackFrame.visibleVariableByName("i");
////            Value value1 = stackFrame.getValue(localVariable1);
////            int i = ((IntegerValue) value1).intValue();
////
////            System.out.println("Debugger print[" + date + " : " + i + "]");
////            eventSet.resume();
//        } else if (event instanceof MethodEntryEvent) {
//            MethodEntryEvent mee = (MethodEntryEvent) event;
//            Method method = mee.method();
//            System.out.println(method.name() + " was Entered!");
//            eventSet.resume();
//        } else if (event instanceof VMDisconnectEvent) {
//            vmExit = true;
//        } else if (event instanceof ClassPrepareEvent) {
//            ClassPrepareEvent classPrepareEvent = (ClassPrepareEvent) event;
//            String mainClassName = classPrepareEvent.referenceType().name();
//            if (mainClassName.equals("test.Test")) {
//                System.out.println("Class " + mainClassName + " is already prepared");
//            }
//            if (true) {
//                // Get location
//                ReferenceType referenceType = classPrepareEvent.referenceType();
//                List locations = referenceType.locationsOfLine(34);
//                Location location = (Location) locations.get(0);
//
//                // Create BreakpointEvent
//                BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(location);
//                breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
//                breakpointRequest.enable();
//            }
//            eventSet.resume();
//        } else {
//            eventSet.resume();
//        }
//    }
//
//    private  static Map<String,DebuggerCmd> map=new HashMap<>();
//
//    @Override
//    public void exe(CmdVo cmd, Map<String, String> param) {
//        String op=param.get("op");
//        String var=param.get("var");
//        String debuggerName=param.get("debugger");
//        if(StringUtil.isEmpty(debuggerName)){
//            debuggerName="default";
//        }
//        DebuggerCmd debugger=map.get(debuggerName);
//        if("init".equalsIgnoreCase(op)){
//            String ip=param.get("ip");
//            String port=param.get("port");
//            if(debugger!=null){
//                debugger.stop();
//            }
//            debugger=new DebuggerCmd();
//            debugger.init(ip,Integer.parseInt(port));
//            Thread t=new Thread(debugger);
//            t.start();
//            map.put(debuggerName,debugger);
//            cmdHandle.getVars().put(var,"success");
//        }else if("setBreakPoint".equalsIgnoreCase(op)){
//            String className=param.get("className");
//            String lineNum=param.get("lineNum");
//            debugger.setBreakPoint(className,Integer.parseInt(lineNum));
//            cmdHandle.getVars().put(var,"success");
//        }else if("cancelBreakPoint".equalsIgnoreCase(op)){
//            String className=param.get("className");
//            String lineNum=param.get("lineNum");
//            debugger.cancelBreakPoint(className,Integer.parseInt(lineNum));
//            cmdHandle.getVars().put(var,"success");
//        }else if("getData".equalsIgnoreCase(op)){
//            String className=param.get("className");
//            String lineNum=param.get("lineNum");
//            String key=param.get("key");
//            Value v=debugger.getData(className,Integer.parseInt(lineNum),key);
//            cmdHandle.getVars().put(var,v.toString());
//        }else if("cancelData".equalsIgnoreCase(op)){
//            debugger.cancelData();
//            cmdHandle.getVars().put(var,"success");
//        }else if("stop".equalsIgnoreCase(op)){
//            debugger.stop();
//            cmdHandle.getVars().put(var,"success");
//        }
//
//    }
//
//    @Override
//    public String op() {
//        return null;
//    }
//
//    @Override
//    public void run() {
//        while (!vmExit){
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
