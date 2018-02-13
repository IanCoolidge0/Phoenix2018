package org.usfirst.frc.team2342.robot;

import org.usfirst.frc.team2342.automodes.leftscaleleftside;
import org.usfirst.frc.team2342.automodes.leftscalerightside;
import org.usfirst.frc.team2342.automodes.leftswitchleft;
import org.usfirst.frc.team2342.automodes.middleleftside;
import org.usfirst.frc.team2342.automodes.middlerightside;
import org.usfirst.frc.team2342.automodes.rightscaleleft;
import org.usfirst.frc.team2342.automodes.rightscaleright;
import org.usfirst.frc.team2342.automodes.rightswitchright;
import org.usfirst.frc.team2342.commands.DriveGamepad;
import org.usfirst.frc.team2342.robot.subsystems.WestCoastTankDrive;
import org.usfirst.frc.team2342.util.Constants;
import org.usfirst.frc.team2342.util.FMS;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */

public class Robot extends IterativeRobot {

	Joystick gamepad = new Joystick(0);
	PCMHandler PCM = new PCMHandler(11);
	WPI_TalonSRX talonFR = new WPI_TalonSRX(Constants.RIGHT_MASTER_TALON_ID);
	WPI_TalonSRX talonFL = new WPI_TalonSRX(Constants.LEFT_MASTER_TALON_ID);
	WPI_TalonSRX talonBR = new WPI_TalonSRX(Constants.RIGHT_SLAVE_TALON_ID);
	WPI_TalonSRX talonBL = new WPI_TalonSRX(Constants.LEFT_SLAVE_TALON_ID);
	WestCoastTankDrive westCoast = new WestCoastTankDrive(PCM, talonFL, talonFR, talonBL, talonBR);
	Joystick joystickR = new Joystick(2);
	Joystick joystickL = new Joystick(1);


    public Robot() {
    	//PCM.turnOn();
    	//WPI_TalonSRX talon1 = new WPI_TalonSRX(0);
    	//WPI_TalonSRX talon2 = new WPI_TalonSRX(1);
    	//boxManipulator = new BoxManipulator(talon1, talon2, PCM);
    	//cascadeElevator = new CascadeElevator(talon1, talon2);
    }
    
    public void teleopInit() {
    	PCM.turnOn();
    	Command driveJoystick = new DriveGamepad(gamepad, westCoast);
    	Scheduler.getInstance().add(driveJoystick);
    }
    
    public void teleopPeriodic() {
    	Scheduler.getInstance().run();
    	//Drive with joystick control in velocity mode
		westCoast.outputToSmartDashboard();
		//Buttons 8 & 9 or (gamepad) 5 & 6 are Low & High gear, respectively
		if (gamepad.getRawButton(5))
			westCoast.setLowGear();
		else if (gamepad.getRawButton(6))
			westCoast.setHighGear();
		else
			westCoast.setNoGear();
		//Sleep for 0.01s
		/*try {
		    Thread.sleep(100);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		//teliopInity
		if (joystick1.getRawButton(1)) {
			talon1.goDistance(0.25, 0.4);
			talon2.goDistance(-0.25, 0.4);
			talon3.goDistance(0.25, 0.4);
			talon4.goDistance(-0.25, 0.4);
		}*/

		// PCM.turnOn();
		// WPI_TalonSRX talon1 = new WPI_TalonSRX(0);
		// WPI_TalonSRX talon2 = new WPI_TalonSRX(1);
		// boxManipulator = new BoxManipulator(talon1, talon2, PCM);
		// cascadeElevator = new CascadeElevator(talon1, talon2);
	}
    
    public void disabledInit() {
    	westCoast.setVelocity(0.0d, 0.0d);
    	westCoast.zeroSensors();
    	Scheduler.getInstance().removeAll();
    }
    
    public void autonomousInit() {
    	FMS.init();
    	//calculate auto mode
    	switch(FMS.getPosition()){
    	case 1:
    		if(FMS.scale()){
    			//go for scale left side
    			Scheduler.getInstance().add(new leftscaleleftside(westCoast));
    		}else if(FMS.teamSwitch()){
    			//go for switch on left side
    			Scheduler.getInstance().add(new leftswitchleft(westCoast));
    		}else{
    			//go for switch on right side
    			Scheduler.getInstance().add(new leftscalerightside(westCoast));
    		}
    	case 2:
    		if(FMS.teamSwitch()){
        		//middle to left side
    			Scheduler.getInstance().add(new middleleftside(westCoast));
        	}else{
        		//middle to right side
        		Scheduler.getInstance().add(new middlerightside(westCoast));
        	}
    	case 3:
    		if(!FMS.scale()){
    			//go for scale right side
    			Scheduler.getInstance().add(new rightscaleright(westCoast));
    		}else if(!FMS.teamSwitch()){
    			//go for switch on right side
    			Scheduler.getInstance().add(new rightswitchright(westCoast));
    		}else{
    			//go for switch on left side
    			Scheduler.getInstance().add(new rightscaleleft(westCoast));
    		}
		default:
			break;
    	}
    	
    }
    
    public void autonomousPeriodic(){
    	Scheduler.getInstance().run();
    	PCM.compressorRegulate();
    	westCoast.outputToSmartDashboard();
    }
    
    @Override
    public void testInit() {
    	westCoast.zeroSensors();
    }
    
    @Override
    public void testPeriodic() {
    	westCoast.setVelocity(-500d, -500d);
    	System.out.println("Angle: " + String.valueOf(westCoast.pidc.getCurAngle()));
    	System.out.println("PID: " + String.valueOf(westCoast.pidc.getCorrection()));
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
