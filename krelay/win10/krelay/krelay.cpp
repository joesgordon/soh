// krelay.cpp : This file contains the 'main' function. Program execution begins
// and ends there.
//

#include <iostream>
#include <string>
#include <exception>

#include "ftd2xx.h"

using std::cout;
using std::endl;
using std::runtime_error;
using std::string;

void set_relays(const int &mask);
void check_status(const FT_STATUS &status);
string status_to_string(const FT_STATUS &status);

int main(int argc, char **argv)
{
    if (argc != 2)
    {
        cout << "ERROR: Wrong number of arguments" << endl;
        return -1;
    }

    int mask = atoi(argv[1]);

    if (mask < 0 || mask > 255)
    {
        cout << "Mask out of bounds (" << mask << ")" << endl;
        return -1;
    }

    try
    {
        set_relays(mask);
    }
    catch (runtime_error &ex)
    {
        cout << "ERROR: " << ex.what() << endl;
        return -1;
    }

    cout << "Exiting" << endl;
}

void set_relays(const int &mask)
{
    DWORD numDevs;
    FT_STATUS status;

    status = FT_CreateDeviceInfoList(&numDevs);
    check_status(status);

    FT_DEVICE_LIST_INFO_NODE *devices = new FT_DEVICE_LIST_INFO_NODE[numDevs];

    status = FT_GetDeviceInfoList(devices, &numDevs);
    check_status(status);

    cout << "Found " << numDevs << " devices." << endl;

    for (DWORD i = 0; i < numDevs; i++)
    {
        cout << "\t device[" << i
             << "].description = " << devices[i].Description << endl;
        cout << "\t device[" << i << "].serial = " << devices[i].SerialNumber
             << endl;
        cout << "\t device[" << i << "].ID = " << devices[i].ID << endl;
        cout << "\t device[" << i << "].handle = " << devices[i].ftHandle
             << endl;
    }

    if (numDevs == 0)
    {
        throw runtime_error("No devices found");
    }
    else if (numDevs > 1)
    {
        throw runtime_error("Too many devices found");
    }

    FT_HANDLE handle;

    status = FT_Open(0, &handle);
    check_status(status);

    status = FT_SetBaudRate(handle, 9600);
    check_status(status);

    status = FT_SetBitMode(handle, (UCHAR)0xFF, (UCHAR)0xFF);
    check_status(status);

    DWORD written = 0;
    BYTE bmask = (BYTE)mask;
    status = FT_Write(handle, &bmask, 1, &written);
    check_status(status);

    if (written != 1)
    {
        throw runtime_error("Didn't write 1 byte");
    }

    // status = FT_Close(handle);
    // check_status(status);
}

void check_status(const FT_STATUS &status)
{
    if (status != FT_OK)
    {
        throw runtime_error(status_to_string(status).c_str());
    }
}

string status_to_string(const FT_STATUS &status)
{
    switch (status)
    {
    case FT_OK:
        return "OK";

    case FT_INVALID_HANDLE:
        return "Invalid Handle";

    case FT_DEVICE_NOT_FOUND:
        return "Device Not Found";

    case FT_DEVICE_NOT_OPENED:
        return "Device Not Opened";

    case FT_IO_ERROR:
        return "I/O Error";

    case FT_INSUFFICIENT_RESOURCES:
        return "Insufficient Resources";

    case FT_INVALID_PARAMETER:
        return "Invalid Parameter";

    case FT_INVALID_BAUD_RATE:
        return "Invalid Baud Rate";

    case FT_DEVICE_NOT_OPENED_FOR_ERASE:
        return "Device Not Opened For Erase";

    case FT_DEVICE_NOT_OPENED_FOR_WRITE:
        return "Device Not Opened For Write";

    case FT_FAILED_TO_WRITE_DEVICE:
        return "Failed to Write Device";

    case FT_EEPROM_READ_FAILED:
        return "EEPROM Read Failed";

    case FT_EEPROM_WRITE_FAILED:
        return "EEPROM Write Failed";

    case FT_EEPROM_ERASE_FAILED:
        return "EEPROM Erase Failed";

    case FT_EEPROM_NOT_PRESENT:
        return "EEPROM not present";

    case FT_EEPROM_NOT_PROGRAMMED:
        return "EEPROM Not Programmed";

    case FT_INVALID_ARGS:
        return "Invalid Args";

    case FT_NOT_SUPPORTED:
        return "Not Supported";

    case FT_OTHER_ERROR:
        return "Other Error";

    case FT_DEVICE_LIST_NOT_READY:
        return "Device List Not Ready";

    default:
        return "Unknown Status... Weird";
    }

    return "How did the code event get here?";
}
