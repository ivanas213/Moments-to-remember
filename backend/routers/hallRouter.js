import express from 'express'
import {upload, getAll} from "../controllers/hallControler.js"
const router = express.Router()

router.post("/upload", upload )
router.get('/getAll', getAll )

export default router