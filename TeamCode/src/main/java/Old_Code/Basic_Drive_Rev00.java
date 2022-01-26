/* Code Generated by Team 16595, based off of 2019-2020 SkyStone Robot Configuration
Basic Driver controlled period operation of Robot, Code has the following Mechanism's
    1) Mecanum Drive - all directions using Controller 1 Left control stick
    2) Speed boost to increase drive speed - using Controller 1 Left bumper
    3) Intake system rotation - Rotate CW CCW using Controller 2 Button Y and X
    4) Rotate system rotation - Rotate CW CCW using Controller 2 Button A and B
    5) Move servo Arm - Open or close servo using Controller 2 Left and Right Bumper
Rev00 - Base Code - September 17,2020
*/

package Old_Code; // Overall Package to be Used by Code

//Libraries Used in code
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

// Start of Opmode, Name shows up on phone, group tells you what type of program is running
// Class name must match name of Code
//@TeleOp(name="Basic_Drive", group="TeleOp")
public class Basic_Drive_Rev00 extends LinearOpMode {

    // Declare Global Variables

    // Start of Opmode
    @Override
    public void runOpMode() {

        // Local Variables, Actuators and Sensors
        ElapsedTime runtime = new ElapsedTime();
        DcMotor leftbackDrive;
        DcMotor rightbackDrive;
        DcMotor leftfrontDrive;
        DcMotor rightfrontDrive;
        DcMotorSimple leftIntake;
        DcMotorSimple rightIntake;
        DcMotorSimple RotateIntake;
        Servo servoArm;
        double servoArmDown = 0.5;
        double servoArmUp = -0.5;
        double maxPower;
        double leftIntakePower;
        double rightIntakePower;
        double RotatePower;
        double backleftPower;
        double backrightPower;
        double frontleftPower;
        double frontrightPower;
        double drive;
        double turn;
        double mechanum;
        boolean intake;
        boolean rotate;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftbackDrive  = hardwareMap.get(DcMotor.class, "leftbackdrive");
        rightbackDrive = hardwareMap.get(DcMotor.class, "rightbackdrive");
        leftfrontDrive  = hardwareMap.get(DcMotor.class, "leftfrontdrive");
        rightfrontDrive = hardwareMap.get(DcMotor.class, "rightfrontdrive");
        leftIntake = hardwareMap.get(DcMotorSimple.class, "leftIntake");
        rightIntake = hardwareMap.get(DcMotorSimple.class, "rightIntake");
        RotateIntake = hardwareMap.get(DcMotorSimple.class, "RotateIntake");
        servoArm = hardwareMap.get(Servo.class, "servoarm");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftbackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightbackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftfrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightfrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        rightIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        RotateIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        servoArm.setPosition(servoArmDown);
        servoArm.setDirection(Servo.Direction.REVERSE) ;

        // Display the Status of Code - At this point Robot is ready to Start
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Read Joystick Values for robot movement
            drive = -gamepad1.left_stick_y;
            turn = gamepad1.left_stick_x;
            mechanum = gamepad1.right_stick_x;

            // Determine if normal speed or boost speed active
            if (gamepad1.left_bumper) {
                maxPower = 0.6;
            } else {
                maxPower = 0.3;
            }

            // Set the motor speed for each wheel based of controller input
            // The values are ranged from 1 to -1 since adding three values want power to have maximum and minimum range
            backleftPower = Range.clip(drive + turn - mechanum, -1.0 * maxPower, maxPower);
            backrightPower = Range.clip(drive - turn + mechanum, -1.0 * maxPower, maxPower);
            frontleftPower = Range.clip(drive + turn + mechanum, -1.0 * maxPower, maxPower);
            frontrightPower = Range.clip(drive - turn - mechanum, -1.0 * maxPower, maxPower);

            // Send calculated power to wheels
            leftbackDrive.setPower(backleftPower);
            rightbackDrive.setPower(backrightPower);
            leftfrontDrive.setPower(frontleftPower);
            rightfrontDrive.setPower(frontrightPower);

            //Intake Direction and Power Based off Buttons
            if (gamepad2.y) {
                intake = true;
                leftIntakePower = 0.6;
                rightIntakePower = 0.6;
            } else if(gamepad2.x){
                intake = true;
                leftIntakePower = -1.0;
                rightIntakePower = -1.0;
            } else if (gamepad2.back){
                intake = false;
                leftIntakePower = 0;
                rightIntakePower = 0;
            } else {
                intake = false;
                leftIntakePower = 0;
                rightIntakePower = 0;
            }
            leftIntake.setPower(leftIntakePower);
            rightIntake.setPower(rightIntakePower);

            //Rotation Direction and Power Based off Buttons
            if(gamepad2.a) {
                rotate = true;
                RotatePower = 0.8;
            } else if(gamepad2.b) {
                rotate = true;
                RotatePower = -0.6;
            } else if (gamepad2.start){
                rotate = false;
                RotatePower = 0;
            } else {
                rotate = false;
                RotatePower = 0;
            }
            RotateIntake.setPower(RotatePower);

            // Move Servo Arm to Given positions
            if (gamepad2.left_bumper) {
                servoArm.setPosition(servoArmUp);
            } else if(gamepad2.right_bumper){
                servoArm.setPosition(servoArmDown);
            }

            // Show the elapsed game time, wheel and intake system power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("FrontMotors", "left (%.2f), right (%.2f)", frontleftPower, frontrightPower);
            telemetry.addData("BackMotors", "left (%.2f), right (%.2f)", backleftPower, backrightPower);
            telemetry.addData("Intake", "Status (%.2f)", intake);
            telemetry.addData("Rotation", "Status (%.2f)", rotate);
            telemetry.addData("ServoArm Position", "left (%.2f)", servoArm.getPosition());
            telemetry.update();
        }

        // Set Power to Zero on all actuators
        RotateIntake.setPower(0);
        leftIntake.setPower(0);
        rightIntake.setPower(0);
        leftbackDrive.setPower(0);
        rightbackDrive.setPower(0);
        leftfrontDrive.setPower(0);
        rightfrontDrive.setPower(0);

    }

}

