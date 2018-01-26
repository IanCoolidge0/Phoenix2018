package org.usfirst.frc.team2342.robot.subsystems;

import org.usfirst.frc.team2342.json.Json;
import org.usfirst.frc.team2342.json.JsonHelper;
import org.usfirst.frc.team2342.json.PIDGains;
import org.usfirst.frc.team2342.loops.Looper;
import org.usfirst.frc.team2342.robot.PCMHandler;
import org.usfirst.frc.team2342.util.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Talon;

public class WestCoastTankDrive extends Subsystem {
    
    private static WestCoastTankDrive mInstance = new WestCoastTankDrive();
    private WPI_TalonSRX leftMaster, rightMaster, leftSlave, rightSlave;
    private PCMHandler PCM;
    
    Json config = JsonHelper.getConfig();
    
    public static WestCoastTankDrive getInstance() {
        return mInstance;
    }
    
    private WestCoastTankDrive() {
        leftMaster = new WPI_TalonSRX(Constants.LEFT_MASTER_TALON_ID);
        rightMaster = new WPI_TalonSRX(Constants.RIGHT_MASTER_TALON_ID);
        leftSlave = new WPI_TalonSRX(Constants.LEFT_SLAVE_TALON_ID);
        rightSlave = new WPI_TalonSRX(Constants.RIGHT_SLAVE_TALON_ID);
        
        leftSlave.follow(leftMaster);
        rightSlave.follow(rightMaster);
        
        leftMaster.configNominalOutputForward(0, 0);
        leftMaster.configNominalOutputReverse(0, 0);
        rightMaster.configPeakOutputForward(1, 0);
        rightMaster.configPeakOutputReverse(-1, 0);
        
        // TODO are these the right indices of the talons?
        PIDGains leftVelocityGains = config.talons.get(0).velocityGains;
        PIDGains leftDistanceGains = config.talons.get(0).distanceGains;
        PIDGains rightVelocityGains = config.talons.get(1).velocityGains;
        PIDGains rightDistanceGains = config.talons.get(1).distanceGains;
        
        WestCoastTankDrive.loadGains(leftMaster, Constants.TALON_VELOCITY_SLOT_IDX, leftVelocityGains);
        WestCoastTankDrive.loadGains(leftMaster, Constants.TALON_DISTANCE_SLOT_IDX, leftDistanceGains);
        WestCoastTankDrive.loadGains(rightMaster, Constants.TALON_VELOCITY_SLOT_IDX, rightVelocityGains);
        WestCoastTankDrive.loadGains(rightMaster, Constants.TALON_DISTANCE_SLOT_IDX, rightDistanceGains);
        
        PCM = new PCMHandler(Constants.PCM_PORT);
        
        zeroSensors();
        
    }
    
    public void setOpenLoop(double left, double right) {
        if (!leftMaster.getControlMode().equals(ControlMode.PercentOutput)) {
            leftMaster.configNominalOutputForward(0, 0);
            rightMaster.configNominalOutputForward(0, 0);
            leftMaster.configNominalOutputReverse(0, 0);
            rightMaster.configNominalOutputReverse(0, 0);
        }
        leftMaster.set(ControlMode.PercentOutput, left);
        rightMaster.set(ControlMode.PercentOutput, right);
    }
    
    public void setVelocity(double left, double right) {
        if (!leftMaster.getControlMode().equals(ControlMode.Velocity)) {
            leftMaster.selectProfileSlot(Constants.TALON_VELOCITY_SLOT_IDX, 0);
        }
        leftMaster.set(ControlMode.Velocity, left);
        rightMaster.set(ControlMode.Velocity, right);
    }
    
    public void setDistance(double left, double right) {
       if (!leftMaster.getControlMode().equals(ControlMode.Position)) {
           leftMaster.selectProfileSlot(Constants.TALON_DISTANCE_SLOT_IDX, 0);
       }
       leftMaster.set(ControlMode.Position, left);
       rightMaster.set(ControlMode.Position, right);
    }
    
    @Override
    public void outputToSmartDashboard() {
        // TODO Auto-generated method stub
    }

    @Override
    public void stop() {
        setOpenLoop(0, 0);
    }

    @Override
    public void zeroSensors() {
        WestCoastTankDrive.zeroEncoders(leftMaster);
        WestCoastTankDrive.zeroEncoders(rightMaster);
        WestCoastTankDrive.zeroEncoders(leftSlave);
        WestCoastTankDrive.zeroEncoders(rightSlave);
    }

    @Override
    public void registerEnabledLoops(Looper enabledLooper) {
        // TODO Auto-generated method stub
    }
    
    public void setHighGear() {
        PCM.setHighGear(true);
        PCM.setLowGear(false);
    }
    
    public void setLowGear() {
        PCM.setHighGear(false);
        PCM.setLowGear(true);
    }
    
    private static void zeroEncoders(WPI_TalonSRX talon) {
        talon.setSelectedSensorPosition(0, Constants.TALON_VELOCITY_SLOT_IDX, 0);
        talon.setSelectedSensorPosition(0, Constants.TALON_DISTANCE_SLOT_IDX, 0);
    }
    
    private static void loadGains(WPI_TalonSRX talon, int slotIdx, PIDGains gains) {
        talon.config_kP(slotIdx, gains.p, 0);
        talon.config_kI(slotIdx, gains.i, 0);
        talon.config_kD(slotIdx, gains.d, 0);
        talon.config_kF(slotIdx, gains.ff, 0);
        talon.config_IntegralZone(slotIdx, gains.izone, 0);
    }

}
