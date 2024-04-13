/*
 * Copyright (c) 2006-2024, RT-Thread Development Team
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Change Logs:
 * Date           Author       Notes
 * 2024-04-13     RT-Thread    first version
 */

#include <rtthread.h>
#include <rtdevice.h>
#include <drv_common.h>

#define DBG_TAG "main"
#define DBG_LVL DBG_LOG
#include <rtdbg.h>

#define LED0_PIN    GET_PIN(C, 0)

rt_uint8_t mode = 0;

void led_entry(void *params) {
    while(1) {
        mode = !mode;
        rt_pin_write(LED0_PIN, mode);
        rt_thread_mdelay(20);
    }
}

rt_thread_t thread;

int main(void)
{
    rt_pin_mode(LED0_PIN, PIN_MODE_OUTPUT);
    thread = rt_thread_create("led", led_entry, RT_NULL, 1024, 20, 5);
    rt_thread_startup(thread);
    return RT_EOK;
}
