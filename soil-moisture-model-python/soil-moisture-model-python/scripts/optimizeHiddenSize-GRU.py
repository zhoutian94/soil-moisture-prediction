import pandas as pd
import numpy as np
import joblib
import torch
import torch.nn as nn
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error
from gru_model import GRUModel

# 加载数据
data = pd.read_csv('../data/csv/merged_data.csv')

# 选择数值型列
numeric_cols = data[['pre', 'gts', 'sm', 'differential_delay']].select_dtypes(include=[np.number])

# 处理缺失值（使用均值替换）
numeric_cols.fillna(numeric_cols.mean(), inplace=True)

# 归一化处理
scaler = MinMaxScaler()
scaled_data = scaler.fit_transform(numeric_cols)

# 保存标准化器
joblib.dump(scaler, 'scaler.pkl')

# 创建时间序列数据
def create_dataset(data, time_step=1):
    X, y = [], []
    for i in range(len(data) - time_step):
        X.append(data[i:(i + time_step), :])  # 过去 `time_step` 天
        y.append(data[i + time_step, 2])  # 土壤含水量是第三列
    return np.array(X), np.array(y)

# 设定时间步长
time_step = 7  # 使用过去7天的数据预测未来1天
X, y = create_dataset(scaled_data, time_step)

# 拆分训练集和测试集
train_size = int(len(X) * 0.8)  # 80%的数据作为训练集
X_train, X_test = X[:train_size], X[train_size:]  # 划分训练集和测试集的特征数据
y_train, y_test = y[:train_size], y[train_size:]  # 划分训练集和测试集的目标数据

# 将NumPy数组转换为PyTorch张量
X_train_tensor = torch.FloatTensor(X_train)
y_train_tensor = torch.FloatTensor(y_train).view(-1, 1)
X_test_tensor = torch.FloatTensor(X_test)
y_test_tensor = torch.FloatTensor(y_test).view(-1, 1)

# 获取输入特征数
input_size = X_train.shape[2]
num_layers = 1
num_epochs = 100
learning_rate = 0.1

# 存储每个隐藏层单元数对应的RMSE值
rmse_values = []

for hidden_size in range(1, 50):
    # 实例化模型
    model = GRUModel(input_size, hidden_size, num_layers)

    # 定义损失函数和优化器
    criterion = nn.MSELoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate)

    # 训练模型
    for epoch in range(num_epochs):
        model.train()
        optimizer.zero_grad()
        outputs = model(X_train_tensor)
        loss = criterion(outputs, y_train_tensor)
        loss.backward()
        optimizer.step()

    # 评估模型
    model.eval()
    with torch.no_grad():
        y_pred_tensor = model(X_test_tensor)

    # 反归一化
    y_pred = scaler.inverse_transform(np.concatenate((np.zeros((y_pred_tensor.shape[0], 2)), y_pred_tensor.numpy(), np.zeros((y_pred_tensor.shape[0], 1))), axis=1))[:, 2]
    y_test = scaler.inverse_transform(np.concatenate((np.zeros((y_test_tensor.shape[0], 2)), y_test_tensor.numpy(), np.zeros((y_test_tensor.shape[0], 1))), axis=1))[:, 2]

    # 计算RMSE
    rmse = np.sqrt(mean_squared_error(y_test, y_pred))
    rmse_values.append((hidden_size, rmse))

# 找到最优隐藏层单元数
optimal_hidden_size = min(rmse_values, key=lambda x: x[1])[0]
print(f'最优隐藏层单元数: {optimal_hidden_size}')
print(f'所有隐藏层单元数对应的RMSE值: {rmse_values}')

# 注释掉绘图代码
# print("预测值:", y_pred[:5])
# print("真实值:", y_test[:5])

# # 假设 y_test 和 y_pred 已经计算好
# # 生成日期范围
# dates = pd.date_range(start='2024-09-14', periods=len(y_test))

# # 创建 DataFrame 以便于绘图
# df = pd.DataFrame({
#     'Date': dates,
#     'Actual': y_test,
#     'Predicted': y_pred
# })

# # 绘制折线图
# plt.figure(figsize=(10, 6))
# plt.plot(df['Date'], df['Actual'], label='实际土壤含水量', marker='o')
# plt.plot(df['Date'], df['Predicted'], label='预测土壤含水量', marker='x')
# plt.xlabel('日期')
# plt.ylabel('土壤含水量 (m3/m3)')
# plt.title('预测值与实际值对比')
# plt.legend()
# plt.grid(True)
# plt.xticks(rotation=45)
# plt.tight_layout()
# plt.show()

# 计算评估指标（如RMSE-Root Mean Squared Error，均方根误差），一种常用的回归模型评估指标，用于衡量预测值与实际值之间的差异
from sklearn.metrics import mean_squared_error, mean_absolute_error, mean_absolute_percentage_error

rmse = np.sqrt(mean_squared_error(y_test, y_pred))
mae = mean_absolute_error(y_test, y_pred)
mse = mean_squared_error(y_test, y_pred)
mape = mean_absolute_percentage_error(y_test, y_pred)

print(f'RMSE: {rmse}')
print(f'MAE: {mae}')
print(f'MSE: {mse}')
print(f'MAPE: {mape}')

#保存模型
torch.save(model.state_dict(), 'sm-model.pth')

# 预测未来7天的土壤含水量
# 假设有最新7天的数据
# 创建DataFrame
data = {
    'pre': [0.000253414, 0.042466424, 0, 0.013785034, 0, 0.7146022, 0.15852802],
    'gts': [293.6274572, 293.7911731, 295.6754032, 296.1911857, 298.0680444, 296.7113155, 289.5321951],
    'sm': [0.260608912, 0.258853835, 0.256352317, 0.253559093, 0.250131076, 0.249054117, 0.250471338],
    'differential_delay': [1126.980279, 1130.437798, 1140.982769, 1149.878125, 1154.166115, 1156.282277, 1124.194953]
}

latest_data = pd.DataFrame(data)
print(latest_data)
latest_data_scaled = scaler.transform(latest_data)

# 进行预测
predictions = []
for _ in range(7):  # 预测未来x天
    latest_tensor = torch.FloatTensor(latest_data_scaled).view(1, time_step, -1)
    pred = model(latest_tensor)
    y_pred_temp = scaler.inverse_transform(
        np.concatenate((np.zeros((pred.shape[0], 2)), pred.detach().numpy(), np.zeros((pred.shape[0], 1))), axis=1))[:,
                  2]
    predictions.append(y_pred_temp.item())

    # 更新latest_data，添加新预测值
    new_row = np.array([pred.item(), 0, 0, 0])  # 用0填充其他列
    latest_data_scaled = np.append(latest_data_scaled[1:], [new_row], axis=0)  # 更新输入数据

print("未来7天的土壤含水量预测:", predictions)

# 注释掉可视化代码
# plt.plot(y_test, label='实际土壤含水量')
# plt.plot(y_pred, label='预测土壤含水量')
# plt.legend()
# plt.show()