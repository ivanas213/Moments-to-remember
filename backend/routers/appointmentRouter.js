import express from "express";
import {getProcessedAppointments, add, getCartForUser, deleteAppointment, sendRequest, acceptRequest, rejectRequest, appoint, getPendingAppointments, getNotificationsForUser} from '../controllers/appointmentController.js'

const router = express.Router()

router.get('/getProcessed', getProcessedAppointments);
router.post('/add', add)
router.get('/getCart/:userId', getCartForUser)
router.delete('/delete/:appointmentId', deleteAppointment)
router.patch('/send/:appointmentId', sendRequest)
router.post('/accept/:appointmentId', acceptRequest)
router.post('/reject/:appointmentId', rejectRequest)
router.post('/appoint', appoint)
router.get('/getPending', getPendingAppointments)
router.get('/getNotifications/:userId', getNotificationsForUser)

export default router