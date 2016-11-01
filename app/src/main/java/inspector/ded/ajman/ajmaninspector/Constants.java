/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package inspector.ded.ajman.ajmaninspector;

/**
 * Defines several constants used between {@link BluetoothPrintService} and the UI.
 */
public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Message types sent from the PhoneReceiver Handler
    public static final int MESSAGE_INCOMING_CALL_STARTED = 6;
    public static final int MESSAGE_OUTGOING_CALL_STARTED = 7;
    public static final int MESSAGE_MISSED_CALL = 8;
    public static final int MESSAGE_INCOMING_CALL_ENDED_ = 9;
    public static final int MESSAGE_OUTGOING_CALL_ENDED = 10;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

}
