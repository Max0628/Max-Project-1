//global variable to store the color or size right now, for others function to use these variable.
let selectedColor = null;
let selectedSize = null;
let productData = {}; // use to store user selection


//get Id from URL
function getProductIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    console.log(params)
    return params.get('id');
}

// Fetch the product details for the ID from the URL
const productId = getProductIdFromURL();
if (productId) {
    fetchProductDetailsById(productId);
} else {
    console.error('Product ID not found in URL.');
}

//fetch product by id
async function fetchProductDetailsById(id) {
    try {
        const response = await fetch(`http://52.194.71.73/api/1.0/products/details?id=${id}`);
        const data = await response.json();
        const product = data.data;
        productData = product;

        if (!productData.colors) {
            console.error('Product colors are missing');
        } else {
            console.log('Product colors:', productData.colors);
        }

        //took out element from product and insert into HTML
        document.getElementById('product-title').textContent = product.title;
        document.getElementById('product-id').textContent = `產品編號：${product.id}`;
        document.getElementById('product-price').textContent = `TWD ${product.price}`;
        document.getElementById('product-note').textContent = `${product.note}`;
        document.getElementById('product-texture').textContent = `材質：${product.texture}`;
        document.getElementById('product-wash').textContent = `清洗方式：${product.wash}`;
        document.getElementById('product-place').textContent = `產地：${product.place}`;
        document.getElementById('product-description').textContent = `細部描述：${product.description}`;
        document.getElementById('main-image').src = product.main_image;

        //send a color obj to function
        setupColorOptions(product.colors);

        //send a size string to function
        setupSizeOptions(product.sizes);

        //add additional pics
        const additionalImages = document.getElementById('additional-images');
        additionalImages.innerHTML = ''; // clean up old pics
        product.images.forEach(imageUrl => {
            const img = document.createElement('img');
            img.className = 'additional-image';
            img.src = imageUrl;
            additionalImages.appendChild(img);//append into html element
        });
    } catch (error) {
        console.error(`Error fetching product ID ${id}:`, error);
    }
}

//starting rendering colors
//while clicking the divs will rerender again
function setupColorOptions(colors) {
    const colorOptions = document.getElementById('color-options');
    colorOptions.innerHTML = ''; // clean up old colors obj
    colors.forEach(color => {
        const colorDiv = document.createElement('div');
        colorDiv.className = 'color-sample'; //set this div name to "color-sample"
        colorDiv.style.backgroundColor = `#${color.code}`; // change this div background color to color hex code.
        colorDiv.title = color.name;
        //clear all selected color status,make sure this color div is clean
        colorDiv.addEventListener('click', () => {
            document.querySelectorAll('.color-sample').forEach(el => el.classList.remove('selected'));
            colorDiv.classList.add('selected');//add selected class on the clicked div
            selectColor(color.code);
            console.log('Selected color:', selectedColor);
        });
        colorOptions.appendChild(colorDiv);
    });
}

//render size
function setupSizeOptions(sizes) {
    const sizeOptions = document.getElementById('size-options');
    sizeOptions.innerHTML = '';
    const sortedSizes = sortSizes(sizes);

    sortedSizes.forEach(size => {
        const sizeDiv = document.createElement('div');
        sizeDiv.className = 'size-sample';
        sizeDiv.textContent = size;
        sizeDiv.addEventListener('click', () => {
            document.querySelectorAll('.size-sample').forEach(el => el.classList.remove('selected'));
            sizeDiv.classList.add('selected');
            selectSize(size);
        });
        sizeOptions.appendChild(sizeDiv);
    });
}

//sort the size in the right order.
function sortSizes(sizes) {
    const sizeOrder = ['S', 'M', 'L', 'XL', 'XXL'];
    return sizes.sort((a, b) => sizeOrder.indexOf(a) - sizeOrder.indexOf(b));
}

function isSizeInStock() {
    return productData.variants.some(variant =>
        variant.color_code === selectedColor && variant.size === selectedSize && variant.stock > 0
    );
}

//wile click the "colorDiv" will save the colorCode at that moment,refresh color and checkout button.
function selectColor(colorCode) {
    selectedColor = colorCode;
    console.log('Selected color:', selectedColor);
    updateSizeAvailability();//update the size first,this part is VERY IMPORTANT!!
}

function selectSize(size) {
    selectedSize = size;
    console.log('Selected size:', selectedSize);
    updateCheckoutButton();
    updateQuantityLimit();
}

//更新size條件
function updateSizeAvailability() {
    const sizeDivs = document.querySelectorAll('.size-sample');
    sizeDivs.forEach(sizeDiv => {
        const size = sizeDiv.textContent;
        //make sure the variant is the right one.
        const variant = productData.variants.find(v => v.color_code === selectedColor && v.size === size);
        //make sure the variant is existed and stock is not zero.
        if (variant && variant.stock > 0) {
            sizeDiv.style.opacity = '1';
            sizeDiv.style.pointerEvents = 'auto';
            sizeDiv.style.cursor = 'pointer';
        } else {
            sizeDiv.style.opacity = '0.5';
            sizeDiv.style.pointerEvents = 'none';
        }
    });
}

//更新數量條件
function updateQuantityLimit() {
    const quantityInput = document.getElementById('quantity');
    const variant = productData.variants.find(v => v.color_code === selectedColor && v.size === selectedSize);
    if (variant) {
        quantityInput.max = variant.stock
        if (parseInt(quantityInput.value) > variant.stock) {
            quantityInput.value = variant.stock;
        }
    }
}

//更新下單按鈕條件
function updateCheckoutButton() {
    const checkoutButton = document.querySelector('.checkout-button');
    if (selectedColor && selectedSize && isSizeInStock()) {
        checkoutButton.disabled = false;
        checkoutButton.style.backgroundColor = '#000';
        checkoutButton.style.cursor = 'pointer';
    } else {
        checkoutButton.disabled = true;
        checkoutButton.style.backgroundColor = '#ccc';
        checkoutButton.style.cursor = 'not-allowed';
    }
}

//limit the number customer can buy.
document.addEventListener('DOMContentLoaded', function () {
    const quantityInput = document.getElementById('quantity');
    const decreaseButton = document.getElementById('decrease');
    const increaseButton = document.getElementById('increase');
    updateCheckoutButton();
    const tapPayDiv = document.getElementById('tap-pay-div');
    tapPayDiv.style.display = 'none';

    function updateQuantity(change) {
        let currentValue = parseInt(quantityInput.value) || 1;
        let newValue = currentValue + change;

        if (newValue >= parseInt(quantityInput.min) && newValue <= parseInt(quantityInput.max)) {
            quantityInput.value = newValue;
        }
    }

    decreaseButton.addEventListener('click', function () {
        updateQuantity(-1);
    });

    increaseButton.addEventListener('click', function () {
        updateQuantity(1);
    });
});

// end of view rendering.
//================================================================================================================================
// while press the pay button,check jwt logic
//checkJWT->press checkout button->enter credit card code-> finished
async function checkJWTAndProceed() {
    const token = localStorage.getItem('jwt');
    const tapPayDiv = document.getElementById('tap-pay-div');

    if (!token) {
        alert('付款前請先登錄唷!');
        window.location.href = 'profile.html';
        return;
    }


    try {
        const response = await fetch('http://52.194.71.73/api/1.0/user/profile', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        alert('驗證成功，請輸入信用卡資訊');

        tapPayDiv.style.display = 'block';

    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        alert('無法獲取個人資料，請稍後重試');
    }
}

document.querySelector('.checkout-button').addEventListener('click', checkJWTAndProceed);


document.getElementById('submit-button').addEventListener('click', function (event) {
    event.preventDefault();

    const tappayStatus = TPDirect.card.getTappayFieldsStatus();
    if (tappayStatus.canGetPrime === false) {
        alert('Unable to get prime. Please check your credit card information.');
        return;
    }

    TPDirect.card.getPrime(function (result) {
        if (result.status !== 0) {
            alert('Failed to get prime: ' + result.msg);
            return;
        }
        const prime = result.card.prime;
        console.log('Successfully got prime: ' + prime);
        submitOrder(prime);
    });
});


async function submitOrder(prime) {
    const orderData = getOrderData();
    orderData.prime = prime;
    printOrderList();

    try {
        const response = await fetch('http://52.194.71.73/api/1.0/order/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('jwt')}`
            },
            body: JSON.stringify(orderData)
        });

        if (response.ok) {
            const responseData = await response.json();
            alert('Order submitted successfully');
            window.location.href = 'thankyou.html';
            console.log("orderData", orderData);
        } else {
            const errorData = await response.json();
            console.error('Order submission failed:', errorData);
            alert('Order submission failed. Please try again.');
        }
    } catch (error) {
        console.error('An error occurred:', error);
        alert('An error occurred. Please try again.');
    }
}


function getOrderData() {
    return {
        prime: null,
        order: {
            shipping: "delivery",
            payment: "credit_card",
            subtotal: 3200,
            freight: 14,
            total: 3214,
            recipient: {
                name: "Luke",
                phone: "0987654321",
                email: "luke@gmail.com",
                address: "市政府站",
                time: "morning"
            },
            list: [
                {
                    id: "4",
                    name: "優雅印花洋裝",
                    price: 3200,
                    color: {
                        code: "ffa6c9",
                        name: "粉紅色"
                    },
                    size: "M",
                    qty: 0
                }
            ]
        }
    };
}


function printOrderList() {
    const orderData = getOrderData();
    console.log('Current Order List:');
    console.log(JSON.stringify(orderData.order.list, null, 2));
}


TPDirect.setupSDK(12348, 'app_pa1pQcKoY22IlnSXq5m5WP5jFKzoRG58VEXpT7wU62ud7mMbDOGzCYIlzzLF', 'sandbox');

TPDirect.card.setup({
    fields: {
        number: {
            element: '#card-number',
            placeholder: '**** **** **** ****'
        },
        expirationDate: {
            element: '#card-expiration-date',
            placeholder: 'MM / YY'
        },
        ccv: {
            element: '#card-ccv',
            placeholder: 'CCV'
        }
    },
    styles: {
        'input': {
            'color': 'gray'
        },
        '.valid': {
            'color': 'green'
        },
        '.invalid': {
            'color': 'red'
        }
    },
    isMaskCreditCardNumber: true,
    maskCreditCardNumberRange: {
        beginIndex: 6,
        endIndex: 11
    }
});

TPDirect.card.onUpdate(function (update) {
    const submitButton = document.getElementById('submit-button');
    if (update.canGetPrime) {
        submitButton.removeAttribute('disabled');
    } else {
        submitButton.setAttribute('disabled', true);
    }
});

