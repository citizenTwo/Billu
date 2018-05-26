import os
import random
from tkinter import *
from tkinter.ttk import *

import firebase_admin
import matplotlib.pyplot as plt
import pandas as pd
import seaborn
from firebase_admin import db

# cred = credentials.Certificate("social-cb91c-firebase-adminsdk-hwkp4-ce2ce05a2d.json")
# firebase_admin.initialize_app(cred)

os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'social-cb91c-firebase-adminsdk-hwkp4-ce2ce05a2d.json'

firebase_admin.initialize_app(options={
    'databaseURL': 'https://social-cb91c.firebaseio.com/'
})
Root = db.reference('social-cb91c')
inventory_ref = Root.child('inventory')

# ds = pd.read_csv('flipkart_ecommerce_try.csv')
# imageURL = ""

rootdb = db.reference()

# for index, row in ds.iterrows():
# inventory_ref.set({
#     row['uniq_id']: {
#         'uniq_id': row['uniq_id'],
#     'product_url': row['product_url']
#     }})

# for image in row['image'].split(','):
#     imageURL = image
#     break

# Update a child attribute of the new user.
# new_user.update({'since': 1799})

# Obtain a new reference to the user, and retrieve child data.
# Result will be made available as a Python dict.
# mary = db.reference('users/{0}'.format(new_user.key)).get()
# print('Name:', mary['name'])
# print('Since:', mary['since'])

root = Tk()
root.title("Social Bridge Inventory System")
root.geometry("1200x600")

# s = Style()
# s.configure("ThemeRed", background="red")

mainFrame = Frame(root)
mainFrame.place(height="50", width="1200", x="0", y="0")

ds = pd.read_csv('flipkart_ecommerce_try.csv')


# listbox = Listbox(root)
# tableFrame = Scrollbar(root)


def inventory(self):
    # tableFrame.place_forget();
    # listbox.place_forget();

    def insertData():
        def insertQuery():
            id = str(ItemID.get())
            url = str(ItemURL.get())
            name = str(ItemName.get())
            pid = str(ProductID.get())
            rp = str(RetailPrice.get())
            dp = str(DiscountPrice.get())
            des = str(Description.get())
            brand = str(Brand.get())
            imageURL = str(ImageURL.get())

            # print("insertedQuery: " + id + " " + name + " " + type)

            #
            # Here you insert into the database
            # row['uniq_id']: {
            #     'uniq_id': row['uniq_id'],
            #     'product_url': row['product_url']
            # }

            new_item = rootdb.child('inventory').push({
                'uniq_id': id,
                'product_url': url,
                'product_name': name,
                'pid': pid,
                'retail_price': rp,
                'discounted_price': dp,
                'description': des,
                'brand': brand,
                'image': imageURL
            })

            insertWindow.destroy()

        insertWindow = Tk()
        insertWindow.title("Insert Data")

        Label(insertWindow, text="New Product :D", font=(Combobox, 30)).grid(row="1", column="2", columnspan="2",
                                                                             padx="4", pady="20", sticky=W)

        Label(insertWindow, text="Item ID").grid(row="2", column="0", columnspan="2", padx="4", pady="4", sticky=W)
        ItemID = Entry(insertWindow, width="50")
        ItemID.grid(row="2", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Item URL").grid(row="3", column="0", columnspan="2", padx="4", pady="4",
                                                  sticky=W)
        ItemURL = Entry(insertWindow, width="50")
        ItemURL.grid(row="3", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Item Name").grid(row="4", column="0", columnspan="2", padx="4", pady="4",
                                                   sticky=W)
        ItemName = Entry(insertWindow, width="50")
        ItemName.grid(row="4", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Product ID").grid(row="5", column="0", columnspan="2", padx="4", pady="4",
                                                    sticky=W)
        ProductID = Entry(insertWindow, width="50")
        ProductID.grid(row="5", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Retial Price").grid(row="6", column="0", columnspan="2", padx="4", pady="4",
                                                      sticky=W)
        RetailPrice = Entry(insertWindow, width="50")
        RetailPrice.grid(row="6", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Discounted Price").grid(row="7", column="0", columnspan="2", padx="4", pady="4",
                                                          sticky=W)
        DiscountPrice = Entry(insertWindow, width="50")
        DiscountPrice.grid(row="7", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Description").grid(row="8", column="0", columnspan="2", padx="4", pady="4",
                                                     sticky=W)
        Description = Entry(insertWindow, width="50")
        Description.grid(row="8", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Brand").grid(row="9", column="0", columnspan="2", padx="4", pady="4",
                                               sticky=W)
        Brand = Entry(insertWindow, width="50")
        Brand.grid(row="9", column="2", columnspan="3", padx="4", pady="4")

        Label(insertWindow, text="Image URL").grid(row="10", column="0", columnspan="2", padx="4", pady="4",
                                                   sticky=W)
        ImageURL = Entry(insertWindow, width="50")
        ImageURL.grid(row="10", column="2", columnspan="3", padx="4", pady="4")

        submitButton = Button(insertWindow, text="Insert item", command=insertQuery)
        submitButton.grid(row="11", column="0", columnspan="8", sticky=EW, padx="4", pady="4")

    # print("1")
    inventoryframe = Frame(root)
    inventoryframe.place(height="550", width="1200", x="0", y="50")
    # inventoryframe.destroy();

    tableFrame = Scrollbar(root)
    # tableFrame.place(height="500", width="1200", x="200", y="50")
    tableFrame.pack(side=RIGHT, fill=Y)

    listbox = Listbox(root)
    listbox.place(height="500", width="1200", x="200", y="50")
    listbox.pack(side=RIGHT, fill=BOTH, expand="true", padx="100", pady="100")

    # text = "Wow"
    listbox.delete(0, END)
    # for i in range(0, 1000):
    #     text = "      |      "
    #     for j in range(0, 5):
    #         text += data[i % 7][j % 6] + "      |      "
    #
    #     listbox.insert(END, text)

    for index, row in ds.iterrows():
        # inventory_ref.set({
        #     row['uniq_id']: {
        #         'uniq_id': row['uniq_id'],
        #     'product_url': row['product_url']
        #     }})
        #
        # for image in row['image'].split(','):
        #     imageURL = image
        #     break

        listbox.insert(END,
                       # row['uniq_id'] + "      |       " +
                       # row['product_url'] + "      |       " +
                       row['product_name']
                       # row['pid'] + "      |       " +
                       # row['retail_price'] + "      |       " +
                       # row['discounted_price'] + "      |       " +
                       # row['description'] + "      |       "  +
                       # row['brand'] + "      |       "
                       # imageURL)
                       )
        # row['uniq_id']
        # row['product_url']
        # row['product_name']
        # row['pid']
        # row['retail_price']
        # row['discounted_price']
        # row['description']
        # row['brand']
        # imageURL

        # for i in range(0, 100):
        #     for j in range (0, 9):
        #         print("  |   " + data[i][j])

        # new_item = root.child('inventory').push({
        #     'uniq_id': row['uniq_id'],
        #     'product_url': row['product_url'],
        #     'product_name': row['product_name'],
        #     'pid': row['pid'],
        #     'retail_price': row['retail_price'],
        #     'discounted_price': row['discounted_price'],
        #     'description': row['description'],
        #     'brand': row['brand'],
        #     'image': imageURL
        # })
    listbox.config(yscrollcommand=tableFrame.set)
    tableFrame.config(command=listbox.yview)

    insertButton = Button(inventoryframe, text="Insert new item to inventory", command=insertData)
    insertButton.pack(fill="x", ipady="10", padx="100", pady="5", side="bottom")


def biller(self):
    # tableFrame.place_forget();
    # listbox.place_forget();

    billName = StringVar()
    billNumber = StringVar()
    billStatus = StringVar()
    # print("2")
    Billerframe = Frame(root)
    Billerframe.place(height="50", width="1200", x="0", y="550")

    def emulateScanner(self):
        print("destroyed")

        tableFrame = Scrollbar(root)
        tableFrame.place(height="400", width="800", x="100", y="250")
        tableFrame.pack(side=RIGHT, fill=Y)

        listbox = Listbox(root)
        listbox.place(height="400", width="800", x="100", y="0")
        listbox.pack(side=RIGHT, fill=BOTH, expand="true", padx="100", pady="150")

        listbox.delete(0, END)
        i = 0

        for index, row in ds.iterrows():

            for image in row['image'].split(','):
                imageURL = image
                break

            if index % 17 == 0:
                listbox.insert(END, row['product_name'])

                rootdb.child('customer_bill').push({
                    'product_name': row['product_name'],
                    'product_price': row['discounted_price'],
                    'product_id': row['pid']
                })

                i += 1
                if i > 4:
                    break

        # rootdb.child('customer_bill').update({
        #     'name' : "Hakuna Maa Tata",
        #     'people' : "Timon and Pumba"
        # })

        rootdb.update({
            'checkout_step': "2"
        })
        # data = (
        #     ("45417", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
        #     ("45418", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
        #     ("45419", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
        #     ("45420", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
        #     ("45421", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
        #     ("45422", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
        #     ("45423", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
        # )

        # text = "Wow"
        # for i in range(0, 1000):
        #     text = "      |      "
        #     for j in range(0, 5):
        #         text += data[i % 7][j % 6] + "      |      "
        #
        # listbox.insert(END, text)

        listbox.config(yscrollcommand=tableFrame.set)
        tableFrame.config(command=listbox.yview)

        billButton.destroy()

    # Billerframe.pack(side="bottom")
    # Billerframe.destroy();

    def RefrestStatus(self):

        # print("refresh")

        informationFrame = Frame(root)
        informationFrame.place(height="100", width="1200", x="0", y="50")

        Label(informationFrame, text="Customer Name", font=(Combobox, 16)).grid(row="0", column="0", sticky=W)
        Label(informationFrame, text=" : ", font=(Combobox, 16)).grid(row="0", column="1", sticky=W)
        CustomerName = Label(informationFrame, textvariable=billName, font=(Combobox, 16))

        Label(informationFrame, text="Bill number", font=(Combobox, 16)).grid(row="1", column="0", sticky=W)
        Label(informationFrame, text=" : ", font=(Combobox, 16)).grid(row="1", column="1", sticky=W)
        CustomerBill = Label(informationFrame, textvariable=billNumber, font=(Combobox, 16))

        Label(informationFrame, text="Payment", font=(Combobox, 16)).grid(row="2", column="0", sticky=W)
        Label(informationFrame, text=" : ", font=(Combobox, 16)).grid(row="2", column="1", sticky=W)
        CustomerStatus = Label(informationFrame, textvariable=billStatus, font=(Combobox, 16))

        rootdb = db.reference()
        value = str(rootdb.child('checkout_step').get())

        print("checkout_step : " + value)

        billName.set(rootdb.child('customer_name').get())
        billStatus.set(rootdb.child('payment_status').get())

        if value == '1':
            num = random.randint(1000, 9999)
            billNumber.set(num)

        if value == '3':
            rootdb.child('customer_bill').delete()
            rootdb.update({
                'checkout_step': "0",
                'customer_name': "",
                'payment_status': ""
            })

            billNumber.set("")

        CustomerBill.grid(row="1", column="2", sticky=W)
        CustomerStatus.grid(row="2", column="2", sticky=W)
        CustomerName.grid(row="0", column="2", sticky=W)

    RefrestStatus(self)

    # inventoryfram        e = Frame(root)
    # inventoryframe.place(height="550", width="1200", x="0", y="50")

    billButton = Button(Billerframe, text="Emulate Scanner")
    billButton.pack(ipady="10", pady="5", side="left", expand="true", fill="x")
    billButton.bind("<Button-1>", emulateScanner)

    refreshButton = Button(Billerframe, text="Refresh")
    refreshButton.pack(ipady="10", pady="5", side="left", expand="true", fill="x")
    refreshButton.bind("<Button-1>", RefrestStatus)


def trends(self):
    # print("3")

    counter = 0

    trendsframe = Frame(root)
    trendsframe.place(height="600", width="1200", x="0", y="50")

    details = StringVar()

    dataDetails = Label(trendsframe, textvariable=details, font=(Combobox, 18))
    details.set("Shubh Laabh")

    def refreshStatus(self):

        dataset = pd.read_csv('discounted_price_plot.csv')

        nonlocal counter

        if counter == 0:
            plt.close()
            details.set("Counter is 1")
            details.set("Items vs. Price")
            seaborn.set(style="ticks")
            seaborn.regplot(x="serial", y="discounted_price", data=dataset)
            fig = plt.gcf()
            fig.canvas.set_window_title('Social Bridge Inventory System')
            plt.show(block=False)

            print(counter)

        if counter == 1:
            details.set("Customer vs Price")
            dataset = pd.read_csv('customer_plot.csv')
            plt.close()

            seaborn.set(style="ticks")
            seaborn.regplot(x="customer_id", y="discounted_price", data=dataset)
            fig = plt.gcf()
            fig.canvas.set_window_title('Social Bridge Inventory System')
            plt.show(block=False)

            print(counter)

        counter += 1

        if counter == 2:
            counter = 0

    RefreshButton = Button(trendsframe, text="Counter")
    RefreshButton.pack(fill="x", ipady="8", padx="100", expand="true", side="bottom")
    RefreshButton.bind("<Button-1>", refreshStatus)

    dataDetails.pack(fill="x", ipady="10", pady="100", padx="100", side="top")


inventoryButton = Button(mainFrame, text="Inventory")
inventoryButton.pack(side="left", fill="x", ipady="10", pady="2", expand="true")
inventoryButton.bind("<Button-1>", inventory)

BillsButton = Button(mainFrame, text="Biller")
BillsButton.pack(side="left", fill="x", ipady="10", pady="2", expand="true")
BillsButton.bind("<Button-1>", biller)

trendsButton = Button(mainFrame, text="Trends")
trendsButton.pack(side="left", fill="x", ipady="10", pady="2", expand="true")
trendsButton.bind("<Button-1>", trends)

# with open("flipkart_ecommerce_try.csv", 'rt') as f:
#     reader = csv.reader(f)
#     for row in reader:
#         print(row)


# data = {{""},{""},{""}}

# ds = pd.read_csv('flipkart_ecommerce_try.csv')
# for index, row in ds.iterrows():
# inventory_ref.set({
#     row['uniq_id']: {
#         'uniq_id': row['uniq_id'],
#     'product_url': row['product_url']
#     }})
#
# for image in row['image'].split(','):
#     imageURL = image
#     break
#
# data[0][index] = row['uniq_id']
# data[1][index] = row['product_url']
# data[2][index] = row['product_name']
# data[3][index] = row['pid']
# data[4][index] = row['retail_price']
# data[5][index] = row['discounted_price']
# data[6][index] = row['description']
# data[7][index] = row['brand']
# data[8][index] = imageURL
#
# for i in range(0, 100):
#     for j in range (0, 9):
#         print("  |   " + data[i][j])
#
# new_item = root.child('inventory').push({
#     'uniq_id': row['uniq_id'],
#     'product_url': row['product_url'],
#     'product_name': row['product_name'],
#     'pid': row['pid'],
#     'retail_price': row['retail_price'],
#     'discounted_price': row['discounted_price'],
#     'description': row['description'],
#     'brand': row['brand'],
#     'image': imageURL
# })

# data = (
#     ("45417", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
#     ("45418", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
#     ("45419", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
#     ("45420", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
#     ("45421", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
#     ("45422", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
#     ("45423", "rodringof", "CSP L2 Review", "0.000394", "2014-12-19 10:08:12", "2014-12-19 10:08:12"),
# )

root.mainloop()
