//listed all url to fetch
const urls = {
    all: "http://52.194.71.73/api/1.0/products/all",
    women: "http://52.194.71.73/api/1.0/products/women",
    men: "http://52.194.71.73/api/1.0/products/men",
    accessories: "http://52.194.71.73/api/1.0/products/accessories",
};

//default fetch all
(async () => {
    await fetchProducts(urls.all, 'all');
})();

//tie event on link
document.getElementById("women").addEventListener("click", (event) => {
    event.preventDefault();
    fetchProducts(urls.women, 'women');
});

document.getElementById("men").addEventListener("click", (event) => {
    event.preventDefault();
    fetchProducts(urls.men, 'men');
});

document.getElementById("accessories").addEventListener("click", (event) => {
    event.preventDefault();
    fetchProducts(urls.accessories, 'accessories');
});

async function fetchProducts(url, category) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        const products = data.data; //take out the object from data
        const productList = document.getElementById("product-list"); //create a list of product
        productList.innerHTML = ""; //clean up the old page innerHTML

        products.forEach((product) => {
            //create a div className is product-card to show product
            const productCard = document.createElement("div");
            productCard.className = "product-card";

            //get color object from colors
            const colors = product.colors
                .map( //map through the color object
                    (color) =>
                        `<span class="color-sample" style="background-color: #${color.code};"></span>`
                )
                .join("");
            productCard.innerHTML = `
                <a href="product.html?id=${product.id}" class="product-link">
                    <img src="${product.main_image}" alt="${product.title}">
                </a>
                <div class="product-info">
                    ${colors}
                    <h3>${product.title}</h3>
                    <p> TWD ${product.price} </p>
                </div>
            `;
            productList.appendChild(productCard);
        });

        if (category !== 'all') {
            history.pushState(null, '', `?category=${category}`);
        } else {
            history.pushState(null, '', '/index.html');
        }


    } catch (error) {
        console.error("Error fetching data:", error);
    }
}


//Fetching Campaign
const campaignURL = "http://52.194.71.73/api/1.0/marketing/campaigns";

async function fetchCampaignData() {
    try {
        const response = await fetch(campaignURL);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log(data);

        const campaigns = data.data;
        const campaignSection = document.querySelector(".campaign");

        const firstCampaign = campaigns[0];
        if (firstCampaign) {
            const container = document.createElement("div");
            container.style.position = "relative";
            container.style.width = "100%";


            const link = document.createElement("a");
            link.href = "product.html?id=3";

            const img = document.createElement("img");
            img.src = firstCampaign.picture;
            img.alt = firstCampaign.story || "Campaign Image";
            img.style.width = "100%";
            img.style.height = "auto";

            link.appendChild(img);


            const h3 = document.createElement("h3");
            h3.innerHTML = `於是<br>我也想要給你<br>一個那麼美好的自己。<br>不朽《與自己和好如初》`;
            h3.style.position = "absolute";
            h3.style.top = "20%";
            h3.style.left = "10%";
            h3.style.zIndex = "10";
            h3.style.color = "black";
            h3.style.padding = "10px";
            h3.style.fontSize = "24px";
            h3.style.lineHeight = "1.5";
            h3.style.backgroundColor = "rgba(255, 255, 255, 0.7)";

            container.appendChild(link);
            container.appendChild(h3);

            campaignSection.appendChild(container);
        }
        ;

    } catch (error) {
        console.error("Error fetching campaign data:", error);
    }
}

fetchCampaignData();

