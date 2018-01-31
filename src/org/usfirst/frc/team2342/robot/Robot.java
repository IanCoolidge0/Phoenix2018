package org.usfirst.frc.team2342.robot;


import org.usfirst.frc.team2342.util.NetworkTableInterface;

import java.util.ArrayList;

import org.usfirst.frc.team2342.robot.actions.*;
import org.usfirst.frc.team2342.robot.subsystems.WestCoastTankDrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends SampleRobot {
	Joystick joystick1 = new Joystick(1);
	Joystick joystick2 = new Joystick(2);
	WestCoastTankDrive westCoast = WestCoastTankDrive.getInstance();

    @Override
    public void operatorControl() {

    	double y = joystick1.getY();
    	double y2 = joystick1.getRawAxis(3);
    	double speedv = 0.5;
    	while(isEnabled()){
    		y = joystick1.getY();
    		y2 = joystick2.getY();
    		
    		//SmartDashboard.putString("DB/String 0", "t1: " + String.valueOf(talon1.getSelectedSensorPosition(0)));
    		//SmartDashboard.putString("DB/String 1", "t2: " + String.valueOf(talon2.getSelectedSensorPosition(0)));
    		//SmartDashboard.putString("DB/String 2", "t3: " + String.valueOf(talon3.getSelectedSensorPosition(0)));
    		//SmartDashboard.putString("DB/String 3", "t4: " + String.valueOf(talon4.getSelectedSensorPosition(0)));

    		
    		//SmartDashboard.putString("DB/String 1", "y2 (24): " + String.valueOf(y2));
	    	//teliopPeriodic    	
    	}
    	
    }

    @Override
    public void autonomous() {
    	boolean isEnabled = isEnabled();
    	ArrayList<Action> actions = new ArrayList<Action>();
    	
    	actions.add(new DriveAction(0.5d, 0.0d, 3000, "1", "Foward"));
    	actions.add(new DriveAction(0.0d, 0.0d, 3000, "2", "Stop"));
    	actions.add(new DriveAction(-0.3d, 0.0d, 2000, "3", "Backward"));
    	actions.add(new DriveAction(0.0d, 0.0d, 3000, "4", "Stop"));
    	actions.add(new DriveAction(-0.3d, 0.0d, 1000, "5", "Backward"));
    	
    	ActionList actionsL = null;
		try {
			actionsL = new ActionList(actions, joystick1);
		} catch (DependencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isEnabled = false;
		}
		
		actionsL.setButton(1);
    	while(isEnabled && isAutonomous()) {
    		actionsL.execute();
    		SmartDashboard.putString("DB/String 1", "EMS: " + actionsL.getEMS());
    		if (actionsL.getEMS() == true) {
    			actionsL.stopAll();
    			break;
    		}
    		else
    			isEnabled = isEnabled();
    	}
    }

    @Override
    public void test() {
    	while(isEnabled()){
    		//NetworkTableInterface.setValue("test", "firstVar", "sup");
	    	//NetworkTableInterface.setValue("test/nextlevel", "firstVar", 1);
	    	//NetworkTableInterface.setValue("test/nextlevel/wow", "firstVar", "sup");
	    	//SmartDashboard.putString("DB/String 1", NetworkTableInterface.getString("test/nextlevel/wow", "firstVar"));
    		westCoast.setOpenLoop(1.0d, 1.0d);
    	}
		westCoast.setOpenLoop(0.0d, 0.0d);
    }
}
