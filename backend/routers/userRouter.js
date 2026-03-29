import express from 'express';
import {login, updateDataById} from '../controllers/userController.js'
const router = express.Router();

router.post("/login", login)
router.put('/updateData/:id', updateDataById);

export default router