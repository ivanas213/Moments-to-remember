import express from 'express'
import connectDB from './config/db.js'
import userRouter from './routers/userRouter.js'
import cors from "cors"
import promotionRouter from './routers/promotionRouter.js';
import eventRouter from './routers/eventRouter.js'
import offerRouter from './routers/offerRouter.js'
import hallRouter from './routers/hallRouter.js'

const app = express();
const PORT = 4000;

app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));
const router=express.Router()
app.use(router)
app.use(cors());
router.use('/user',userRouter); 
router.use('/promotion', promotionRouter);
router.use('/event', eventRouter)
router.use('/offer', offerRouter)
router.use('/hall', hallRouter)

connectDB().then(() => {
    app.listen(PORT, '0.0.0.0', () => {
        console.log(`Server listening on port ${PORT}...`);
    });
}).catch((error) => {
    console.error('Error during starting server:', error);
});