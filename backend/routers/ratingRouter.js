import express from "express"
import { add, getAll } from "../controllers/ratingController.js"

const router = express.Router()
router.post("/add", add)
router.get("/all/:offerId", getAll);
export default router