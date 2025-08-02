import { CloudinaryStorage } from 'multer-storage-cloudinary';
import multer from 'multer';
import cloudinary from './cloudinary.js';

const storagePromo = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'promo_slike',
    allowed_formats: ['jpg', 'png', 'jpeg'],
  },
});
const storageEvent = new CloudinaryStorage({
  cloudinary: cloudinary,
  params: {
    folder: 'dogadjaji_slike',
    allowed_formats: ['jpg', 'png', 'jpeg'],
  },
});
const uploadPromo = multer({ storage: storagePromo }); 
const uploadEvent = multer({storage: storageEvent})
export {uploadPromo, uploadEvent}
