import express from 'express'
import {Add} from '../controllers/cartItemController.js'

const router = express.Router()

router.post("/add", Add)

export default router