import express from 'express'
import {upload, getAll, add} from '../controllers/offerControler.js'
import { uploadOffer } from "../utils/storage.js";

const router = express.Router()

router.post("/upload", upload)
router.get('/getAll', getAll);
router.post("/add", uploadOffer.single("image"), add);
export default router

